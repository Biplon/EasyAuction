package ea.java.Struct;

public class CoolDownGroup
{
    private final String permission;

    private final int coolDown;

    public String getPermission()
    {
        return permission;
    }

    public int getCoolDown()
    {
        return coolDown;
    }

    public CoolDownGroup(String permission, int coolDown)
    {
        this.permission = permission;
        this.coolDown = coolDown;
    }
}
