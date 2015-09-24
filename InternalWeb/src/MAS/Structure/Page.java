package MAS.Structure;

import java.util.*;

public class Page implements MenuChild {
    private String path;
    private Set<String> permissions;

    public Page(String path, Set<String> permissions) {
        this.path = path;
        this.permissions = permissions;
    }

    public Page(String path, String... permissions) {
        this(path, new HashSet<String>(Arrays.asList(permissions)));
    }

    public String getPath() {
        return path;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public List<String> getPagesList() {
        return new ArrayList<String>(Arrays.asList(path));
    }
}
