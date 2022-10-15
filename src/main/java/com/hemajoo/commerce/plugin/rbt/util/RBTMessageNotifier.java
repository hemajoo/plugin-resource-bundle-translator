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

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import lombok.NonNull;

/**
 * A message notifier for the {@code Resource Bundle Translator}.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class RBTMessageNotifier
{
    private RBTMessageNotifier()
    {
        // Avoid direct instantiation!
    }

    /**
     * Creates a notification message.
     * @param content Message content.
     */
    public static void notify(final @NonNull String content)
    {
        notify(null, content);
    }

    /**
     * Creates a notification message.
     * @param project Project being the owner of the message.
     * @param content Message content.
     */
    public static void notify(final Project project, final @NonNull String content)
    {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
                .createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }

    /**
     * Creates an error message.
     * @param project Project being the owner of the message.
     * @param content Message content.
     */
    public static void error(final Project project, final @NonNull String content)
    {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }
}
