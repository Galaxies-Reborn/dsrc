package script.item;

import script.dictionary;
import script.obj_id;

public class heroic_random_stat_item extends script.base_script
{
    public heroic_random_stat_item()
    {
    }
    public static final String[] LEGACY_NGE_PRIMARY_MODIFIERS =
    {
        "agility_modified",
        "stamina_modified",
        "constitution_modified",
        "precision_modified",
        "strength_modified",
        "luck_modified"
    };
    public static final String[] LEGACY_NGE_ACTION_MODIFIERS =
    {
        "expertise_action_weapon_0",
        "expertise_action_weapon_1",
        "expertise_action_weapon_2",
        "expertise_action_weapon_4",
        "expertise_action_weapon_5",
        "expertise_action_weapon_6",
        "expertise_action_weapon_7",
        "expertise_action_weapon_9",
        "expertise_action_weapon_10",
        "expertise_action_weapon_11"
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
    public static final int WEAPON_SPEED_VALUE = 2;
    public int OnAttach(obj_id self) throws InterruptedException
    {
        removeLegacyNgeModifiers(self);
        if (!hasWeaponSpeedModifier(self))
        {
            messageTo(self, "generateRandomStats", null, 3, false);
        }
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        removeLegacyNgeModifiers(self);
        if (!hasWeaponSpeedModifier(self))
        {
            messageTo(self, "generateRandomStats", null, 3, false);
        }
        return SCRIPT_CONTINUE;
    }
    public int generateRandomStats(obj_id self, dictionary params) throws InterruptedException
    {
        removeLegacyNgeModifiers(self);
        if (!hasWeaponSpeedModifier(self))
        {
            setObjVar(self, "skillmod.bonus." + getWeightedWeaponSpeedModifier(), WEAPON_SPEED_VALUE);
        }
        return SCRIPT_CONTINUE;
    }
    public void removeLegacyNgeModifiers(obj_id self) throws InterruptedException
    {
        removeSkillModifiers(self, LEGACY_NGE_PRIMARY_MODIFIERS);
        removeSkillModifiers(self, LEGACY_NGE_ACTION_MODIFIERS);
    }
    public void removeSkillModifiers(obj_id self, String[] modifiers) throws InterruptedException
    {
        for (String modifier : modifiers)
        {
            String objVar = "skillmod.bonus." + modifier;
            if (hasObjVar(self, objVar))
            {
                removeObjVar(self, objVar);
            }
        }
    }
    public boolean hasWeaponSpeedModifier(obj_id self) throws InterruptedException
    {
        return hasAnySkillModifier(self, RANGED_SPEED_MODIFIERS) ||
            hasAnySkillModifier(self, MELEE_SPEED_MODIFIERS) ||
            hasAnySkillModifier(self, LIGHTSABER_SPEED_MODIFIERS);
    }
    public boolean hasAnySkillModifier(obj_id self, String[] modifiers) throws InterruptedException
    {
        for (String modifier : modifiers)
        {
            if (hasObjVar(self, "skillmod.bonus." + modifier))
            {
                return true;
            }
        }
        return false;
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
