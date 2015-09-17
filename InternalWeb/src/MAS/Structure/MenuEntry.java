package MAS.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MenuEntry<T> {
    private String name;
    private String icon;
    private List<T> children;
    private List<String> permissions;

    public MenuEntry(String name, String icon, List<T> children) {
        this.name = name;
        this.icon = icon;
        this.children = children;
    }

    public MenuEntry(String name, String icon, T... children) {
        this(name, icon, Arrays.asList(children));
    }

    public MenuEntry(String name, String icon) {
        this(name, icon, new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public List<T> getChildren() {
        return children;
    }

    public void add(T child) {
        children.add(child);
    }

    public void updatePermissions() {
        List<String> permissions = new ArrayList<>();
        for(T child : this.children) {
            if (child instanceof MenuEntry) {
                permissions.addAll(((MenuEntry) child).getPermissions());
            }
            if (child instanceof Page) {
                permissions.addAll(((Page) child).getPermissions());
            }
        }
        this.permissions = permissions;
    }

    public List<String> getPermissions() {
        if(this.permissions == null)
            updatePermissions();
        return this.permissions;
    }

    public boolean hasPermission(List<String> permissions) {
        return ! Collections.disjoint(this.permissions, permissions);
    }
}
