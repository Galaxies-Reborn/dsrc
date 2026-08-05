package script.item;

import script.dictionary;
import script.obj_id;

public class heroic_random_stat_item extends script.base_script
{
    public heroic_random_stat_item()
    {
    }
    public static final String[] STAT_ONE =
    {
        "agility_modified",
        "stamina_modified",
        "constitution_modified"
    };
    public static final String[] STAT_TWO =
    {
        "precision_modified",
        "strength_modified",
        "luck_modified"
    };
    public static final String[] RANGED_SPEED_MODIFIERS =
    {
        "rifle_speed",
        "carbine_speed",
        "pistol_speed"
    };
    public static final String[] MELEE_SPEED_MODIFIERS =
    {
        "onehandmelee_speed",
        "twohandmelee_speed",
        "unarmed_speed",
        "polearm_speed"
    };
    public static final String[] LIGHTSABER_SPEED_MODIFIERS =
    {
        "onehandlightsaber_speed",
        "twohandlightsaber_speed",
        "polearmlightsaber_speed"
    };
    public static final int[] STAT_VALS =
    {
        25,
        25,
        2
    };
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, "skillmod.bonus"))
        {
            messageTo(self, "generateRandomStats", null, 3, false);
        }
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, "skillmod.bonus"))
        {
            messageTo(self, "generateRandomStats", null, 3, false);
        }
        return SCRIPT_CONTINUE;
    }
    public int generateRandomStats(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, "skillmod.bonus"))
        {
            setObjVar(self, "skillmod.bonus." + STAT_ONE[rand(0, STAT_ONE.length - 1)], STAT_VALS[0]);
            setObjVar(self, "skillmod.bonus." + STAT_TWO[rand(0, STAT_TWO.length - 1)], STAT_VALS[1]);
            setObjVar(self, "skillmod.bonus." + getWeightedWeaponSpeedModifier(), STAT_VALS[2]);
        }
        return SCRIPT_CONTINUE;
    }
    public String getWeightedWeaponSpeedModifier() throws InterruptedException
    {
        String[] weaponChoices;
        int weightingRoll = rand(0, 100);
        if (weightingRoll <= 57)
        {
            weaponChoices = RANGED_SPEED_MODIFIERS;
        }
        else if (weightingRoll >= 78)
        {
            weaponChoices = LIGHTSABER_SPEED_MODIFIERS;
        }
        else
        {
            weaponChoices = MELEE_SPEED_MODIFIERS;
        }
        return weaponChoices[rand(0, weaponChoices.length - 1)];
    }
}
