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
import org.ressec.core.extension.i18n.translation.engine.ITranslationResultSentence;

//@EqualsAndHashCode
public final class GoogleTranslationResultSentence implements ITranslationResultSentence
{
    @Getter
    @Setter
    @SerializedName("trans")
    private String translation;

    @Getter
    @Setter
    @SerializedName("orig")
    private String original;

    @Getter
    @Setter
    @SerializedName("backend")
    private int backend;

//    @Getter
//    @SerializedName("translation_engine_debug_info")
//    private GoogleTranslationDebugInfo debug;
}
