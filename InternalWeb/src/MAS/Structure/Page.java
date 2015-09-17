package MAS.Structure;

import java.util.Arrays;
import java.util.List;

public class Page {
    private String path;
    private List<String> permissions;

    public Page(String path, List<String> permissions) {
        this.path = path;
        this.permissions = permissions;
    }

    public Page(String path, String... permissions) {
        this(path, Arrays.asList(permissions));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permission) {
        this.permissions = permission;
    }
}
