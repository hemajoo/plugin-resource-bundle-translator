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

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.ressec.core.extension.i18n.translation.engine.ITranslationResult;
import org.ressec.core.extension.i18n.translation.engine.ITranslationResultSentence;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@code Google Free API} translation result.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class GoogleTranslationResult implements ITranslationResult
{
    /**
     * Result translation of sentences.
     */
    @Getter
    @Setter
    @SerializedName("sentences")
    private List<ITranslationResultSentence> sentences;

    /**
     * Source of the translation.
     */
    @Getter
    @Setter
    @SerializedName("src")
    private String source;

    /**
     * Confidence factor of the translation.
     */
    @Getter
    @Setter
    @SerializedName("confidence")
    private double confidence;

    /**
     * Creates a new Google translation result.
     */
    public GoogleTranslationResult()
    {
        // Required for JSON serialization.
    }

    /**
     * Return the translation result.
     * @return Translation result.
     */
    public final String getTranslation()
    {
        return sentences.stream()
                .map(ITranslationResultSentence::getTranslation)
                .collect( Collectors.joining(""));
    }

    @Override
    public TranslationProviderType getProviderType()
    {
        return TranslationProviderType.GOOGLE_FREE_TRANSLATE_API;
    }
}
