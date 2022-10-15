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
package com.hemajoo.commerce.plugin.rbt.util;

import lombok.Getter;
import lombok.NonNull;

/**
 * Concrete implementation of an {@code HTML} string used to display elements in the {@code Resource Bundle Translator}
 * plugin window.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class HTMLString
{
    private static final String HTML_START = "<html>";
    private static final String HTML_END = "</html>";
    private static final String HTML_STYLE_END = "\">";
    private static final String HTML_SPAN_START = "<span style=\"color:";
    private static final String HTML_SPAN_END = "</span>";

    private final StringBuilder builder = new StringBuilder(HTML_START);

    public static String removeHtml(final @NonNull String text)
    {
        String result = text;

        result = result.replace(HTML_START, "");
        result = result.replace(HTML_SPAN_START, "");
        result = result.replace(HTML_STYLE_END, "");
        result = result.replace(HTML_SPAN_END, "");
        result = result.replace(HTML_END, "");

        return result;
    }

    // See: https://material.io/resources/color/#!/?view.left=0&view.right=0
    public enum Color
    {
        RED_50("#ffebee"),
        RED_100("#ffcdd2"),
        RED_200("#ef9a9a"),
        RED_300("#e57373"),
        RED_400("#ef5350"),
        RED_500("#f44336"),
        RED_600("#e53935"),
        RED_700("#d32f2f"),
        RED_800("#c62828"),
        RED_900("#b71c1c"),

        PINK_50("#fce4ec"),
        PINK_100("#f8bbd0"),
        PINK_200("#f48fb1"),
        PINK_300("#f06292"),
        PINK_400("#ec407a"),
        PINK_500("#e91e63"),
        PINK_600("#d81b60"),
        PINK_700("#c2185b"),
        PINK_800("#ad1457"),
        PINK_900("#880e4f"),

        //PURPLE_50(""),
        //PURPLE_DEEP_50(""),
        //INDIGO_50(""),
        //BLUE_50(""),
        //BLUE_LIGHT_50(""),
        //CYAN_50(""),
        //TEAL_50(""),
        //GREEN_50(""),
        //GREEN_LIGHT_50(""),
        //LIME_50(""),
        //YELLOW_50(""),
        //AMBER_50(""),
        //ORANGE_50(""),
        //ORANGE_DEEP_50(""),
        //BROWN_50(""),
        //GREY_50(""),
        //GREY_BLUE_50(""),

        GREY_BLUE_400("#78909c"),


        ORANGE("#e36009");

        @Getter
        private String rgb;

        Color(final @NonNull String rgbValue)
        {
            this.rgb = rgbValue;
        }
    }

    public HTMLString()
    {
    }

    public HTMLString(final @NonNull String value)
    {
        if (!value.isBlank())
        {
            builder.append(value);
        }
    }

    public HTMLString(final @NonNull String rgbColor, final @NonNull String value)
    {
        if (!value.isBlank())
        {
            builder
                    .append(HTML_SPAN_START)
                    .append(rgbColor)
                    .append(HTML_STYLE_END)
                    .append(value)
                    .append(HTML_SPAN_END);
        }
    }

    public HTMLString(final @NonNull HTMLString.Color color, final @NonNull String value)
    {
        if (!value.isBlank())
        {
            builder
                    .append(HTML_SPAN_START)
                    .append(color.getRgb())
                    .append("\">")
                    .append(value)
                    .append(HTML_SPAN_END);
        }
    }

    public void append(final @NonNull String value)
    {
        if (!value.isBlank())
        {
            builder.append(value);
        }
    }

    public void append(final @NonNull String rgbColor, final @NonNull String value)
    {
        if (!value.isBlank())
        {
            builder
                    .append(HTML_SPAN_START)
                    .append(rgbColor)
                    .append("\">")
                    .append(value)
                    .append(HTML_SPAN_END);
        }
    }

    public void append(final @NonNull HTMLString.Color color, final @NonNull String value)
    {
        if (!value.isBlank())
        {
            builder
                    .append(HTML_SPAN_START)
                    .append(color.getRgb())
                    .append("\">")
                    .append(value)
                    .append(HTML_SPAN_END);
        }
    }

    public String toString()
    {
        return builder.toString() + HTML_END;
    }
}
