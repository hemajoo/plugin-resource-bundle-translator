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

import com.hemajoo.commerce.plugin.rbt.util.HTMLString;
import com.intellij.lang.Language;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.PropertiesImplUtil;
import com.intellij.psi.PsiFile;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;

/**
 * Represents a translation file which is a {@link PsiFile} in {@code IntelliJ IDEA} and its underlying property file.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class TranslationFile
{
    /**
     * Properties file type.
     */
    private static final String FILE_TYPE_PROPERTIES = "Properties";

    /**
     * Psi underlying file.
     */
    @Getter
    private final PsiFile file;

    /**
     * File inner name (without locale and without extension).
     */
    @Getter
    private String rootName;

    /**
     * File base name (without extension).
     */
    @Getter
    private String baseName;

    /**
     * File extension.
     */
    @Getter
    private String extension;

    /**
     * Locale of the properties file.
     */
    @Getter
    private final Locale locale;

    /**
     * Creates a new translation file.
     * @param file {@link PsiFile} file.
     */
    public TranslationFile(final @NonNull PsiFile file)
    {
        this.file = file;
        this.locale = parse(file);
    }

    /**
     * Refreshes the underlying {@link PsiFile}.
     */
    public final void refresh()
    {
        file.getVirtualFile().refresh(false, false);
    }

    /**
     * Returns the {@link Locale} of the underlying resource bundle property file.
     * @param file {@link PsiFile} file.
     * @return {@link Locale} or {@link Locale#ENGLISH} (which is the default) if no locale was specified.
     */
    private Locale parse(final @NonNull PsiFile file)
    {
        Locale defaultLocale = new Locale("en", "US", "Default");
        Locale propertyLocale;
        String localePart;

        String[] parts = file.getName().split("\\.");
        if (parts.length > 1)
        {
            baseName = parts[0];
            extension = parts[1];
            rootName = baseName;

            int index = parts[0].lastIndexOf("_");
            if (index != -1)
            {
                localePart = parts[0].substring(index + 1);

                try
                {
                    propertyLocale = new Locale(localePart);
                    propertyLocale.getISO3Language();
                    String[] otherParts = rootName.split("_" + localePart);
                    rootName = otherParts[0];

                    return propertyLocale;
                }
                catch (MissingResourceException mre)
                {
                    return defaultLocale;
                }
            }
        }

        return defaultLocale;
    }

    /**
     * Returns if the translation file is a valid resource bundle property file.
     * @return True if the translation file is valid, false otherwise.
     */
    public final boolean isValid()
    {
        return file.getLanguage() == Language.findLanguageByID(FILE_TYPE_PROPERTIES);
    }

    /**
     * Checks if the given source translation file is related to this translation file?
     * @param source Source {@link TranslationFile}.
     * @return True if the given source translation file is related, false otherwise.
     */
    public final boolean isRelated(final @NonNull TranslationFile source)
    {
        return isValid() && rootName.equals(source.getRootName()) && !source.getFile().getName().equals(file.getName());
    }

    /**
     * Returns the resource bundle properties file associated to this {@link PsiFile}.
     * @return Properties file.
     */
    public final List<IProperty> getProperties()
    {
        return Objects.requireNonNull(PropertiesImplUtil.getPropertiesFile(getFile())).getProperties();
    }

    /**
     * Returns the translation file filename in HTML format with colors.
     * @return String label typically to be used in a JLabel as colored text.
     */
    public final String getUILabel()
    {
        HTMLString html = new HTMLString();

        html.append(file.getName() + " (");
        html.append(HTMLString.Color.GREY_BLUE_400, "language=" + locale.getDisplayLanguage());
        if (!locale.getCountry().isBlank())
        {
            html.append(HTMLString.Color.GREY_BLUE_400, ", country=" + locale.getDisplayCountry());
        }
        if (!locale.getVariant().isBlank())
        {
            html.append(HTMLString.Color.GREY_BLUE_400, ", variant=" + locale.getDisplayVariant());
        }
        html.append(")");

        return html.toString();
    }
}
