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

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import lombok.Getter;
import lombok.NonNull;
import org.ressec.core.extension.i18n.translation.engine.google.GoogleTranslationRequest;

import java.util.Objects;

/**
 * Represents a I18n Google translation request.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class I18nGoogleTranslationRequest extends GoogleTranslationRequest
{
    /**
     * Source translation file.
     */
    @Getter
    private final TranslationFile source;

    /**
     * Target translation file.
     */
    @Getter
    private final TranslationFile target;

    /**
     * Creates a new I18n Google translation request.
     * @param project Project.
     * @param source Source translation file.
     * @param target Target translation file.
     */
    public I18nGoogleTranslationRequest(final @NonNull Project project, final @NonNull TranslationFile source, final @NonNull TranslationFile target)
    {
        this.source = source;
        this.target = target;

        setSourceLocale(source.getLocale());
        setTargetLocale(target.getLocale());

        setSourceProperties(
                Objects.requireNonNull(
                        PsiDocumentManager.getInstance(project).getDocument(source.getFile())).getText());
        setTargetProperties(
                Objects.requireNonNull(
                        PsiDocumentManager.getInstance(project).getDocument(target.getFile())).getText());
    }
}
