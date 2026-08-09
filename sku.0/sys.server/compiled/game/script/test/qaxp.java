package script.test;

import script.dictionary;
import script.library.*;
import script.obj_id;

import java.util.Arrays;
import java.util.HashSet;

public class qaxp extends script.base_script
{
    public qaxp()
    {
    }
    public static final int XP_AMOUNT = 1000000;
    public static final String SCRIPTVAR = "qaxp";
    public static final String PROMPT = "Select the amount of XP you desire in the right box";
    public static final String[] QATOOL_MAIN_MENU = dataTableGetStringColumn("datatables/test/qa_tool_menu.iff", "main_tool");
    public static final String QATOOL_TITLE = "QA Tools";
    public static final String QATOOL_PROMPT = "Choose the tool you want to use";
    public static final int REVOKE_XP = 0;
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!isGod(self) || getGodLevel(self) < 50 || !isPlayer(self)) {
            detachScript(self, "test.qaxp");
        }
        return SCRIPT_CONTINUE;
    }
    public int OnSpeaking(obj_id self, String text) throws InterruptedException
    {
        if (isGod(self))
        {
            if ((toLower(text)).equals(SCRIPTVAR))
            {
                toolMainMenu(self);
                return SCRIPT_OVERRIDE;
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int handleXpOptions(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_CANCEL)
        {
            removePlayer(player, "");
            return SCRIPT_CONTINUE;
        }
        if (btn == sui.BP_REVERT)
        {
            qa.refreshMenu(self, QATOOL_PROMPT, QATOOL_TITLE, QATOOL_MAIN_MENU, "toolMainMenu", true, "qatool.pid");
            utils.removeScriptVarTree(player, SCRIPTVAR);
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx < 0)
        {
            removePlayer(player, "");
            return SCRIPT_CONTINUE;
        }
        if (idx == REVOKE_XP)
        {
            revokeWorkingSkillXp(player);
            return SCRIPT_CONTINUE;
        }
        String[] xpTypes = utils.getStringArrayScriptVar(player, SCRIPTVAR + ".xpTypes");
        int xpIndex = idx - 1;
        if (xpTypes == null || xpIndex < 0 || xpIndex >= xpTypes.length)
        {
            removePlayer(player, "The PRE-CU XP selection is no longer valid.");
            return SCRIPT_CONTINUE;
        }
        showPrecuXpTransfer(player, xpTypes[xpIndex]);
        return SCRIPT_CONTINUE;
    }
    public int handleXpAmountAdd(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = qa.findTarget(self);
        int btn = sui.getIntButtonPressed(params);
        int amt = sui.getTransferInputTo(params);
        String xpType = utils.getStringScriptVar(player, SCRIPTVAR + ".xpType");
        if (btn == sui.BP_CANCEL)
        {
            removePlayer(player, "");
            toolMainMenu(player);
            return SCRIPT_CONTINUE;
        }
        if (xpType == null || xpType.length() == 0 || xp.isRetiredNgeProgressionExperienceType(xpType))
        {
            removePlayer(player, "The selected PRE-CU XP pool is invalid.");
            return SCRIPT_CONTINUE;
        }
        xp.grant(player, xpType, amt, false);
        toolMainMenu(player);
        return SCRIPT_CONTINUE;
    }
    public int handleXpAmountRevoke(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = qa.findTarget(self);
        int btn = sui.getIntButtonPressed(params);
        int amt = sui.getTransferInputTo(params);
        String xpType = utils.getStringScriptVar(player, SCRIPTVAR + ".xpType");
        if (btn == sui.BP_CANCEL)
        {
            removePlayer(player, "");
            toolMainMenu(player);
            return SCRIPT_CONTINUE;
        }
        if (xpType == null || xpType.length() == 0 || xp.isRetiredNgeProgressionExperienceType(xpType))
        {
            removePlayer(player, "The selected PRE-CU XP pool is invalid.");
            return SCRIPT_CONTINUE;
        }
        xp.grantUnmodifiedExperience(player, xpType, -amt, false);
        toolMainMenu(player);
        return SCRIPT_CONTINUE;
    }
    public void toolMainMenu(obj_id player) throws InterruptedException
    {
        String[] xpTypes = getPrecuXpTypes();
        String[] menu = new String[xpTypes.length + 1];
        menu[0] = "Revoke current working-skill XP";
        System.arraycopy(xpTypes, 0, menu, 1, xpTypes.length);
        utils.setScriptVar(player, SCRIPTVAR + ".xpTypes", xpTypes);
        qa.refreshMenu(player, "Select an explicit Publish 14.1 XP pool.", "PRE-CU XP Tool", menu, "handleXpOptions", "qaxp.pid", SCRIPTVAR + ".mainMenu", sui.OK_CANCEL_REFRESH);
    }
    public void removePlayer(obj_id player, String err) throws InterruptedException
    {
        sendSystemMessageTestingOnly(player, err);
        qa.removeScriptVars(player, SCRIPTVAR);
        utils.removeScriptVarTree(player, SCRIPTVAR);
    }
    public String[] getPrecuXpTypes() throws InterruptedException
    {
        HashSet xpTypes = new HashSet();
        String[] professionRoots = skill.getPrecuPublicProfessionRoots();
        for (String professionRoot : professionRoots)
        {
            String[] professionSkills = skill.getPrecuProfessionSkillList(professionRoot);
            if (professionSkills == null)
            {
                continue;
            }
            for (String professionSkill : professionSkills)
            {
                String xpType = skill_template.getSkillExperienceType(professionSkill);
                if (xpType != null && xpType.length() > 0 && !xp.isRetiredNgeProgressionExperienceType(xpType))
                {
                    xpTypes.add(xpType);
                }
            }
        }
        xpTypes.add(xp.SPACE_PRESTIGE_IMPERIAL);
        xpTypes.add(xp.SPACE_PRESTIGE_REBEL);
        xpTypes.add(xp.SPACE_PRESTIGE_PILOT);
        String[] result = new String[xpTypes.size()];
        xpTypes.toArray(result);
        Arrays.sort(result);
        return result;
    }
    public void showPrecuXpTransfer(obj_id player, String xpType) throws InterruptedException
    {
        if (xpType.equals(xp.SPACE_PRESTIGE_IMPERIAL) && !space_flags.isImperialPilot(player))
        {
            removePlayer(player, "The test character does not have the Imperial pilot skill required for this prestige pool.");
            toolMainMenu(player);
            return;
        }
        if (xpType.equals(xp.SPACE_PRESTIGE_REBEL) && !space_flags.isRebelPilot(player))
        {
            removePlayer(player, "The test character does not have the Rebel pilot skill required for this prestige pool.");
            toolMainMenu(player);
            return;
        }
        if (xpType.equals(xp.SPACE_PRESTIGE_PILOT) && !space_flags.isNeutralPilot(player))
        {
            removePlayer(player, "The test character does not have the neutral pilot skill required for this prestige pool.");
            toolMainMenu(player);
            return;
        }
        utils.setScriptVar(player, SCRIPTVAR + ".xpType", xpType);
        sui.transfer(player, player, PROMPT, "PRE-CU XP Tool", "Available", XP_AMOUNT, "Amount", 0, "handleXpAmountAdd");
    }
    public void revokeWorkingSkillXp(obj_id player) throws InterruptedException
    {
        String skillName = getWorkingSkill(player);
        if (!skill.isPrecuPublicProfessionSkillName(skillName))
        {
            removePlayer(player, "Select a public PRE-CU working skill before revoking XP.");
            toolMainMenu(player);
            return;
        }
        String xpType = skill_template.getSkillExperienceType(skillName);
        if (xpType == null || xpType.length() == 0)
        {
            removePlayer(player, "The selected PRE-CU working skill has no XP requirement.");
            toolMainMenu(player);
            return;
        }
        utils.setScriptVar(player, SCRIPTVAR + ".xpType", xpType);
        sui.transfer(player, player, PROMPT, "PRE-CU XP Tool", "Revoke Experience", XP_AMOUNT, "Amount", 0, "handleXpAmountRevoke");
    }
}
