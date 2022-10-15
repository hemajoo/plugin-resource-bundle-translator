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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.gsonfire.GsonFireBuilder;
import lombok.NonNull;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.ressec.core.extension.i18n.translation.engine.*;
import org.ressec.core.foundation.helper.GsonHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Represents a Google translation processor.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class GoogleTranslatorProcessor implements ITranslationProcessor
{
    private static final String GOOGLE_TRANSLATE_API = "https://translate.googleapis.com/translate_a/t?client=dict-chrome-ex&sl=";

    /**
     * Gson builder.
     */
    private final Gson gsonBuilder;

    /**
     * Creates a new Google translation processor.
     */
    public GoogleTranslatorProcessor()
    {
        gsonBuilder = new GsonFireBuilder()
                .createGsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .registerTypeAdapter(ITranslationResult.class, new GoogleTranslationResultDeserializer())
                .registerTypeAdapter(ITranslationResultSentence.class, new GoogleTranslationResultSentenceDeserializer())
                .enableComplexMapKeySerialization()
                .create();
    }

    @Override
    public void translate(final @NonNull ITranslationProcess process, final @NonNull ITranslationRequestEntry entry) throws TranslationException
    {
        translateEach(process, entry);
    }

    /**
     * Translate a request entry.
     * @param process Translation process.
     * @param entry Translation request entry.
     * @throws TranslationException Thrown to indicate an error occurred while trying to translate a request entry.
     */
    private void translateEach(final @NonNull ITranslationProcess process, final @NonNull ITranslationRequestEntry entry) throws TranslationException
    {
        String url;

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build())
        {
            if (entry.requireTranslation())
            {
                url = buildUrl(
                        entry.getSource(),
                        process.getRequest().getSourceLocale().getLanguage(),
                        process.getRequest().getTargetLocale().getLanguage());

                HttpGet http = new HttpGet(url);
                http.setHeader( "Accept", "application/json" );
                HttpResponse response = httpClient.execute(new HttpGet(url));
                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK)
                {
                    entry.setResult(deserializeResponse(getResponseString(response)));
                }
                else
                {
                    throw new TranslationException(response.getStatusLine().getReasonPhrase());
                }
            }
        }
        catch (Exception e)
        {
            throw new TranslationException(e);
        }
   }

    /**
     * Builds the URL to be used for the translation.
     * @param text Text to be translated.
     * @param sourceLanguage Source language.
     * @param targetLanguage Target language.
     * @return Translation URL.
     */
    private String buildUrl(String text, String sourceLanguage, String targetLanguage)
    {
        String textEncoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
        return GOOGLE_TRANSLATE_API + sourceLanguage + "&tl=" + targetLanguage + "&dt=t&q=" + textEncoded + "&dj=1";
    }

    /**
     * Extracts the response string from the received HTTP response.
     * @param response HTTP response.
     * @return Response string.
     * @throws IOException Thrown to indicate an error occurred while trying to extracts the response string.
     */
    private String getResponseString(HttpResponse response) throws IOException
    {
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * Deserializes the response.
     * @param response Response.
     * @return De-serialized {@link ITranslationResult} representing the translation result.
     */
    private ITranslationResult deserializeResponse(String response)
    {
        return GsonHelper.deserialize(
                gsonBuilder,
                response,
                new TypeToken<ITranslationResult>(){}.getType());
    }
}
