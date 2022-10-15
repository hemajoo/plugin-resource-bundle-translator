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
package com.hemajoo.commerce.plugin.rbt.model;

import com.hemajoo.commerce.plugin.rbt.service.RBTService;
import com.hemajoo.commerce.plugin.rbt.util.HTMLString;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;

/**
 * Represents the data model of the {@link RBTService}.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class PropertiesModel
{
    /**
     * Project.
     */
    @Getter
    private final Project project;

    /**
     * Translation files.
     */
    @Getter
    @Setter
    private Map<Locale, TranslationFile> translations = new HashMap<>();

    /**
     * Source translation file.
     */
    @Getter
    @Setter
    private TranslationFile sourceTranslation;

    /**
     * Target translation files.
     */
    @Getter
    @Setter
    private Map<Locale, TranslationFile> targetTranslation = new HashMap<>();

    /**
     * Selected target translation files.
     */
    @Getter
    @Setter
    private Map<Locale, TranslationFile> targetTranslationSelected = new HashMap<>();

    /**
     * Resource bundle properties files.
     */
    @Getter
    private final Map<Locale, Properties> properties = new HashMap<>();

    @Getter
    @Setter
    private int translatedEntries = 0;

//    @Getter
//    @Setter
//    private Map<PsiFile, SelectionModeType> others = new HashMap();

    /**
     * Creates a new properties translation data model entity given a source translation file.
     * @param project Project.
     * @param sourceTranslation Source translation file.
     * @throws PropertiesModelException Thrown in case an error occurred initializing the properties data model.
     */
    public PropertiesModel(final @NonNull Project project,  final @NonNull TranslationFile sourceTranslation) throws PropertiesModelException
    {
        if (sourceTranslation.isValid())
        {
            this.project = project;
            this.sourceTranslation = sourceTranslation;
            translations.put(sourceTranslation.getLocale(), sourceTranslation);
        }
        else
        {
            throw new PropertiesModelException("");
        }
    }

//    /**
//     * Clears the translation files of the data model.
//     */
//    public void clear()
//    {
//        others.clear();
//    }

    /**
     * Refreshes all underlying files in the model.
     */
    public final void refresh()
    {
        sourceTranslation.refresh();
        for (TranslationFile file : targetTranslation.values())
        {
            file.refresh();
        }
    }

    /**
     * Collects all associated resource bundle properties files.
     */
    public void collect()
    {
        TranslationFile target;

        PsiDirectory directory = sourceTranslation.getFile().getParent();
        for (PsiFile file : Objects.requireNonNull(directory).getFiles())
        {
            target = new TranslationFile(file);
            if (target.isRelated(sourceTranslation))
            {
                translations.put(target.getLocale(), target);
            }
        }
    }

    /**
     * Returns the plugin service.
     * @return {@link RBTService}.
     */
    public RBTService getService()
    {
        return project.getService(RBTService.class);
    }

    /**
     * Sets the new source translation file and update the target translation file list.
     * @param name New properties translation source file.
     */
    public final void sourceTranslationChanged(final @NonNull String name) throws PropertiesModelException
    {
        sourceTranslation = getTargetTranslationByName(name);
        targetTranslationSelected.clear();

        for (TranslationFile target : translations.values())
        {
            if (!sourceTranslation.getFile().getName().equals(target.getFile().getName()))
            {
                targetTranslation.put(target.getLocale(), target);
            }
        }
    }

    /**
     * Returns the target translation file given its name.
     * @param name Target translation file name.
     * @return Target {@link TranslationFile}.
     * @throws PropertiesModelException Thrown in case an error occurred while trying to retrieve the target
     * translation file.
     */
    private TranslationFile getTargetTranslationByName(final @NonNull String name) throws PropertiesModelException
    {
        String[] parts = name.split(" \\(");
        String value = HTMLString.removeHtml(parts[0]);

        for (TranslationFile file : translations.values())
        {
            if (value.equals(file.getFile().getName()))
            {
                return file;
            }
        }

        throw new PropertiesModelException(String.format("Cannot find target translation file for name: '%s'", name));
    }

    /**
     * Unselects a target translation file given its name.
     * @param name Target translation file to unselect.
     */
    public final void unselectTarget(final @NonNull String name)
    {
        for (Map.Entry<Locale, TranslationFile> entry : targetTranslationSelected.entrySet())
        {
            if (entry.getValue().getFile().getName().equals(name))
            {
                targetTranslationSelected.remove(entry.getKey());
            }
        }
    }

    /**
     * Selects a target translation file given its name.
     * @param name Target translation file to select.
     */
    public final void selectTarget(final @NonNull String name)
    {
        for (Map.Entry<Locale, TranslationFile> entry : targetTranslation.entrySet())
        {
            if (entry.getValue().getFile().getName().equals(name))
            {
                targetTranslationSelected.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Returns the total number of property entries to translate.
     * @return Number of entries to translate.
     */
    public int getCountEntriesToTranslate()
    {
        int count = 0;

        for (Map.Entry<Locale, Properties> entry : properties.entrySet())
        {
            if (sourceTranslation.getLocale() != entry.getKey())
            {
                count += entry.getValue().size();
            }
        }

        return count;
    }
}
