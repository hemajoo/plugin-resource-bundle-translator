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
package com.hemajoo.commerce.plugin.rbt.action;

import com.hemajoo.commerce.plugin.rbt.message.MessageSelectAll;
import com.hemajoo.commerce.plugin.rbt.model.PropertiesModel;
import com.hemajoo.commerce.plugin.rbt.service.RBTService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.Icons;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Action associated to the {@code I18n Resource Bundle Translator} plugin used to select at once all target resource
 * bundle properties files available in the target panel of the plugin window.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class ActionSelectAll extends AnAction
{
    /**
     * Creates a new action instance.
     */
    public ActionSelectAll()
    {
        super("Select All", "Select all target properties files at once.", Icons.ToolbarSelectAllTarget);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event)
    {
        MessageSelectAll publisher = Objects.requireNonNull(
                event.getProject()).getMessageBus().syncPublisher(
                        MessageSelectAll.MESSAGE_TOPIC_SELECT_ALL_TARGET);
        publisher.selectAllTarget();
    }

    @Override
    public void update(@NotNull AnActionEvent event)
    {
        super.update(event);

        boolean enabled = false;

        if (event.getProject() != null)
        {
            RBTService service = event.getProject().getService(RBTService.class);
            if (service != null)
            {
                PropertiesModel model = service.getModel();
                if (model != null && model.getTargetTranslation() != null && !model.getTargetTranslation().isEmpty())
                {
                    enabled = true;
                }
            }
        }

        event.getPresentation().setEnabled(enabled);
        event.getPresentation().setVisible(true);
    }
}
