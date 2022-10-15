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
package com.hemajoo.commerce.plugin.rbt.translation;

import lombok.NonNull;
import org.ressec.core.extension.i18n.translation.engine.TranslationRequestEntry;

/**
 * Represents a {@code Google Translate API} translation request entry.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class GoogleTranslationRequestEntry extends TranslationRequestEntry
{
    /**
     * Creates a new Google translation request entry.
     * @param key Key of the resource bundle property entry.
     * @param source Source.
     * @param requireTranslation True to indicate this entry requires a translation, false otherwise.
     */
    public GoogleTranslationRequestEntry(final @NonNull String key, final @NonNull String source, final boolean requireTranslation)
    {
        super(key, source, requireTranslation);
    }
}
