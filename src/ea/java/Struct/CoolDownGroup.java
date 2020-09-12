package ea.java.Struct;

public class CoolDownGroup
{
    //permission for this cooldown group
    private final String permission;
    //cooldown time for this cooldown group
    private final int coolDown;

    //getter
    public String getPermission()
    {
        return permission;
    }

    public int getCoolDown()
    {
        return coolDown;
    }

    //constructor
    public CoolDownGroup(String permission, int coolDown)
    {
        this.permission = permission;
        this.coolDown = coolDown;
    }
}
