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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.ressec.core.extension.i18n.translation.engine.ITranslationResultSentence;

import java.lang.reflect.Type;

/**
 * Deserializer to be used when JSON encounters a {@link ITranslationResultSentence} interface. It indicates which
 * concrete implementation is to be used.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class GoogleTranslationResultSentenceDeserializer implements JsonDeserializer<ITranslationResultSentence>
{
    @Override
    public ITranslationResultSentence deserialize(JsonElement json, Type type, JsonDeserializationContext context)
    {
        return context.deserialize(json, GoogleTranslationResultSentence.class);
    }
}
