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

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * About dialog wrapper for {@code Resource Bundle Translator} plugin.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class AboutDialogWrapper extends DialogWrapper
{
    private final AboutDialog dialog = new AboutDialog();

    public AboutDialogWrapper()
    {
        super(true); // use current window as parent
        init();
        setTitle("About");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel()
    {
        return dialog.getPanelMain();
    }
}
