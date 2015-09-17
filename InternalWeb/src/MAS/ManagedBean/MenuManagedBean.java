package MAS.ManagedBean;

import MAS.Structure.MainMenu;
import MAS.Structure.MenuEntry;
import MAS.Structure.Page;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class MenuManagedBean {

    private MainMenu mainMenu = new MainMenu();

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public boolean hasChildren(MenuEntry menuEntry) {
        return menuEntry.getChildren().get(0) instanceof MenuEntry;
    }

    public String getPath(MenuEntry<Page> menuEntry) {
        return menuEntry.getChildren().get(0).getPath();
    }

}
