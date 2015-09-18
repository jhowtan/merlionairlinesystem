package MAS.ManagedBean;

import MAS.Structure.MenuChild;
import MAS.Structure.MenuEntry;
import MAS.Structure.Page;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class MenuManagedBean {

    public boolean hasChildMenuEntry(MenuEntry menuEntry) {
        return menuEntry.getChildren().get(0) instanceof MenuEntry;
    }

    public String getPath(MenuEntry menuEntry) {
        MenuChild firstChild = menuEntry.getChildren().get(0);
        if(firstChild instanceof Page) {
            return ((Page) firstChild).getPath();
        }
        return null;
    }

}
