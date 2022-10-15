/*
 * Copyright(c) 2020 by Resse Christophe.
 * --------------------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -------------------------------------------------------------------------------------
 */
package com.hemajoo.commerce.plugin.rbt.service;

import com.hemajoo.commerce.plugin.rbt.model.I18nGoogleTranslationRequest;
import com.hemajoo.commerce.plugin.rbt.model.PropertiesModel;
import com.hemajoo.commerce.plugin.rbt.model.TranslationFile;
import com.hemajoo.commerce.plugin.rbt.translation.GoogleTranslationProcess;
import com.hemajoo.commerce.plugin.rbt.util.RBTMessageNotifier;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diff.DiffBundle;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.ressec.core.extension.i18n.translation.engine.ITranslationProcess;
import org.ressec.core.extension.i18n.translation.engine.ITranslationRequest;
import org.ressec.core.extension.i18n.translation.engine.ITranslationRequestEntry;
import org.ressec.core.extension.i18n.translation.engine.TranslationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a I18n service which controls the whole translation process.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Service
public final class RBTService
{
    /**
     * Translation processes.
     */
    private final List<ITranslationProcess> processes = new ArrayList<>();

    /**
     * Data model used by the service.
     */
    @Getter
    private PropertiesModel model;

    /**
     * Project.
     */
    @Getter
    private Project project;

    /**
     * Creates a I18n service.
     */
    public RBTService()
    {
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * Sets the properties translation model to use.
     * @param model Properties translation model.
     */
    public void setModel(final @NonNull Project project,  final @NonNull PropertiesModel model)
    {
        this.model = model;
        this.project = project;
        model.collect();
    }

    /**
     * Executes a set of prepared translations.
     */
    public void execute()
    {
        executeBackground();
    }

    /**
     * Executes a set of prepared translations in the background.
     */
    public void executeBackground()
    {
        prepare();

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "I18n property translation")
        {
            @Override
            public void onFinished()
            {
                try
                {
                    save();
                }
                catch (TranslationException e)
                {
                    RBTMessageNotifier.error(project, String.format("Cannot save document due to: %s", e.getMessage()));
                }
            }

            @Override
            public void run(final @NotNull ProgressIndicator indicator)
            {
                try
                {
                    indicator.setIndeterminate(false);
                    indicator.setFraction(0.00);
                    indicator.setText("Computing number of entries to translate...");

                    execute(indicator);

                    indicator.setFraction(1.00);
                    indicator.setText("Translation completed.");

                    RBTMessageNotifier.notify(
                            project,
                            String.format("<b>Translation completed</b>.<br>Property files processed: <b>%d</b>.<br>Request entries translated: <b>%d</b>", processes.size(), getTranslatedCount()));
                }
                catch (TranslationException e)
                {
                    // Whole translations aborted, notification of error already reported!
                    // We still have to save the documents for the successful translations.
                }
            }
        });
    }

    /**
     * Prepares a set of translation processes.
     */
    private void prepare()
    {
        processes.clear();

        ITranslationProcess process;
        ITranslationRequest request;

        // Create the translation processes, one for each file to translate.
        for (Map.Entry<Locale, TranslationFile> entry : getModel().getTargetTranslationSelected().entrySet())
        {
            process = new GoogleTranslationProcess();
            request = new I18nGoogleTranslationRequest(project, getModel().getSourceTranslation(), entry.getValue());
            process.setRequest(request);
            if (process.requireProcessing())
            {
                processes.add(process);
            }
        }
    }

    /**
     * Translates a set of translation processes.
     * @param indicator Progress indicator bar.
     * @throws TranslationException Thrown in case an error occurred while translating a set of translation processes.
     */
    private void execute(final @NotNull ProgressIndicator indicator) throws TranslationException
    {
        int total = 0; // Total number of request entries.
        int remaining = 0; // Remaining request entries to translate.

        // Get the total number of translation processes and the total number of translations to process
        for (ITranslationProcess process : processes)
        {
            total += process.getRequest().getEntries().size();
            remaining += process.getRequest().getCount();
        }

        int totalRemaining = remaining;
        if (remaining != 0)
        {
            for (ITranslationProcess process : processes)
            {
                try
                {
                    if (process.requireProcessing())
                    {
                        for (ITranslationRequestEntry entry : process.getRequest().getEntries())
                        {
                            indicator.setIndeterminate(false);
                            process.getProcessor().translate(process, entry);
                            remaining -= 1;

                            indicator.setText(String.format("Total of request entries: %d. Remaining request entries to translate: %d on a total of: %d", total, remaining, totalRemaining));
                            indicator.setFraction((totalRemaining - remaining) * 100d / totalRemaining);
                        }

                        process.hasBeenTranslated();
                    }
                }
                catch (TranslationException e)
                {
                    RBTMessageNotifier.error(project, String.format("Translation process aborted due to: %s", e.getMessage()));
                    throw e;
                }
            }
        }
        else
        {
            RBTMessageNotifier.notify(project, "<b>No translation to process!</b>");
        }
    }

    /**
     * Saves the associated documents of a set of executed translation processes.
     * @throws TranslationException Thrown in case an error occurred while saving the documents of a set of executed
     * translation processes.
     */
    private void save() throws TranslationException
    {
        I18nGoogleTranslationRequest request;

        // Update for all process the document entity.
        for (ITranslationProcess process : processes)
        {
            // Only save translations that have been processed.
            if (process.isTranslated())
            {
                request = ((I18nGoogleTranslationRequest) process.getRequest());
                final Document document = PsiDocumentManager.getInstance(project).getDocument(request.getTarget().getFile());

                if (document != null)
                {
                    String content = process.getDocument();
                    CommandProcessor.getInstance()
                            .executeCommand(
                                    project,
                                    () -> ApplicationManager.getApplication().runWriteAction(
                                            () -> document.setText(content)),
                                    DiffBundle.message("save.merge.result.command.name"),
                                    document);
                }
            }
        }

        model.refresh();
    }

    /**
     * Returns the number of translated elements.
     * @return Number of translated elements.
     */
    private int getTranslatedCount()
    {
        int count = 0;

        for (ITranslationProcess process : processes)
        {
            for (ITranslationRequestEntry entry : process.getRequest().getEntries())
            {
                if (entry.getTranslationTimeStamp() != null)
                {
                    count += 1;
                }
            }
        }

        return count;
    }
}

