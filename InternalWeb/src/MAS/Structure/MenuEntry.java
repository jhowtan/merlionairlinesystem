package MAS.Structure;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MenuEntry implements MenuChild {
    private String name;
    private String icon;
    private List<MenuChild> children;
    private Set<String> permissions;

    public MenuEntry(String name, String icon, List<MenuChild> children) {
        this.name = name;
        this.icon = icon;
        this.children = children;
        updatePermissions();
    }

    public MenuEntry(String name, String icon, MenuChild... children) {
        this(name, icon, Arrays.asList(children));
    }

    public void updatePermissions() {
        permissions = new HashSet<>();
        for(MenuChild child : this.children) {
            Set<String> childPermissions = child.getPermissions();
            if (childPermissions.size() == 0) {
                permissions.clear();
                return;
            } else {
                permissions.addAll(childPermissions);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public List<MenuChild> getChildren() {
        return children;
    }

    public List<MenuChild> getAccessibleChildren(Set<String> permissions) {
        List<MenuChild> accessibleChildren = new ArrayList<>();
        for (MenuChild menuChild : children) {
            if (menuChild.getPermissions().size() == 0 || ! Collections.disjoint(menuChild.getPermissions(), permissions)) {
                if (menuChild instanceof MenuEntry) {
                    ((MenuEntry) menuChild).stripInaccessibleChildren(permissions);
                }
                accessibleChildren.add(menuChild);
            }
        }
        return accessibleChildren;
    }

    public void stripInaccessibleChildren(Set<String> permissions) {
        this.children = getAccessibleChildren(permissions);
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public List<String> getPagesList() {
        ArrayList<String> pagesList = new ArrayList<>();
        for (MenuChild child : children) {
            pagesList.addAll(child.getPagesList());
        }
        return pagesList;
    }

}
