package script.ai;

import script.dictionary;
import script.library.pet_lib;
import script.obj_id;

public class officer_pet extends script.base_script
{
    public officer_pet()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        messageTo(self, "getAndFollowMaster", null, 3, false);
        messageTo(self, "verifyReinforcementsSkill", null, 3, false);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        messageTo(self, "getAndFollowMaster", null, 3, false);
        messageTo(self, "verifyReinforcementsSkill", null, 3, false);
        return SCRIPT_CONTINUE;
    }
    public int getAndFollowMaster(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id master = getMaster(self);
        if (isIdNull(master))
        {
            return SCRIPT_CONTINUE;
        }
        if (retirePostNgeOfficerPet(self, master))
        {
            return SCRIPT_OVERRIDE;
        }
        setCondition(self, CONDITION_CONVERSABLE);
        pet_lib.doCommandNum(self, pet_lib.COMMAND_FOLLOW, master);
        return SCRIPT_CONTINUE;
    }
    public int verifyReinforcementsSkill(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id master = getMaster(self);
        if (isIdNull(master))
        {
            return SCRIPT_CONTINUE;
        }
        if (retirePostNgeOfficerPet(self, master))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public boolean retirePostNgeOfficerPet(obj_id self, obj_id master) throws InterruptedException
    {
        if (!isIdValid(master) || !isPlayer(master))
        {
            return false;
        }
        pet_lib.destroyOfficerPets(master);
        if (isIdValid(self) && exists(self))
        {
            destroyObject(self);
        }
        return true;
    }
}
