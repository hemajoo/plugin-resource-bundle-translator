package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Provides a global placeholder to access icons for the {@code Resource Bundle Translator} plugin.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public interface Icons
{
    static Icon TranslateToolbarIcon = IconLoader.getIcon("/icons/translate.png");
    static Icon TranslateToolbarIcon2 = IconLoader.getIcon("/icons/i18n.png");
    static Icon ToolbarClear = IconLoader.getIcon("/icons/toolbar/clear.png");
    static Icon ToolbarTranslate = IconLoader.getIcon("/icons/toolbar/translate.png");
    static Icon ToolbarSelectAllTarget = IconLoader.getIcon("/icons/toolbar/selectAll.png");
    static Icon ToolbarAbout = IconLoader.getIcon("/icons/toolbar/help.png");
}
