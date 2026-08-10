package script.working.wwallace;

import script.library.utils;
import script.obj_id;

public class create_space_weapons extends script.base_script
{
    public create_space_weapons()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!isGod(self) || getGodLevel(self) < 50 || !isPlayer(self))
        {
            detachScript(self, "working.wwallace.create_space_weapons");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
    public int OnSpeaking(obj_id self, String strText) throws InterruptedException
    {
        obj_id objInventory = utils.getInventoryContainer(self);
        String[] strCommands = split(strText, ' ');
        if (strCommands[0].equals("createWeapons"))
        {
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_armek_sw4.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_armek_sw7.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_borstel_rg9.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_corellian_1d.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_corellian_ag1g_laser.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_corellian_ag2g_quad_laser.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_gyrhil_auto_blaster.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_gyrhil_r9x.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_incom_blaster.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_incom_disruptor.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_incom_quad_blaster.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_incom_shredder.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_seinar_disruptor.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_seinar_ion_cannon.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_seinar_linked_cannon.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_seinar_ls1.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_seinar_ls72.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_slayn_ioncannon.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_subpro_tripleblaster.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_subpro_tripleblaster_mark2.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_taim_heavy_laser.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_taim_ix4.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_taim_kx5.iff", objInventory);
            createObjectOverloaded("object/tangible/ship/components/weapon/wpn_taim_kx9.iff", objInventory);
        }
        if (strCommands[0].equals("grantPilotSkills"))
        {
            sendSystemMessageTestingOnly(self, "The all-faction pilot grant is retired. Use the authenticated JTL QA or test-center pilot selector and choose one faction.");
        }
        return SCRIPT_CONTINUE;
    }
}
