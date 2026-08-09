package script.systems.loot;

import script.*;
import script.library.*;

public class rare_loot_chest extends script.base_script
{
    private boolean retirePlayerOwnedChest(obj_id self) throws InterruptedException
    {
        if (!loot.isPostNgeRareLootSystemRetired())
        {
            return false;
        }
        obj_id player = utils.getContainingPlayer(self);
        loot.retirePostNgeRareLootPlayerState(player);
        if (isIdValid(player) && exists(player) && isPlayer(player) && isIdValid(self) && exists(self))
        {
            destroyObject(self);
        }
        return true;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        return retirePlayerOwnedChest(self) ? SCRIPT_OVERRIDE : SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        return retirePlayerOwnedChest(self) ? SCRIPT_OVERRIDE : SCRIPT_CONTINUE;
    }
    public int OnTransferred(obj_id self, obj_id sourceContainer, obj_id destContainer, obj_id transferer) throws InterruptedException
    {
        return retirePlayerOwnedChest(self) ? SCRIPT_OVERRIDE : SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        loot.retirePostNgeRareLootPlayerState(player);
        retirePlayerOwnedChest(self);
        return SCRIPT_OVERRIDE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        loot.retirePostNgeRareLootPlayerState(player);
        retirePlayerOwnedChest(self);
        return SCRIPT_OVERRIDE;
    }
}
