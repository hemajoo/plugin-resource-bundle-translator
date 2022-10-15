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
package com.hemajoo.commerce.plugin.rbt.ui;

import lombok.Getter;

import javax.swing.*;

/**
 * About dialog window for the {@code Resource Bundle Translator}.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class AboutDialog
{
    @Getter
    private JPanel panelMain;
    private JLabel htmlTitle;
    private JLabel htmlContent;
    private JLabel pluginIcon;

    private void createUIComponents()
    {
        // Empty
    }
}
