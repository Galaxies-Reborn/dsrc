package script.library;

import script.obj_id;
import script.string_id;

import java.util.Vector;

public class expertise extends script.base_script
{
    public expertise()
    {
    }
    public static final String JEDI_AUTO_ALLOCATION_TABLE = "datatables/expertise/autoallocation_jedi.iff";
    public static final string_id SID_SUI_EXPERTISE_INTRODUCTION_TITLE = new string_id("expertise_d", "sui_expertise_introduction_title");
    public static final string_id SID_SUI_EXPERTISE_INTRODUCTION_BODY = new string_id("expertise_d", "sui_expertise_introduction_body");
    public static void cacheExpertiseProcReacList(obj_id player) throws InterruptedException
    {
        String[] skillModList = getSkillStatModListingForPlayer(player);
        Vector expertiseProcReacList = new Vector();
        expertiseProcReacList.setSize(0);
        for (String s : skillModList) {
            if ((s.startsWith("expertise_") || s.startsWith("kill_meter_")) && (s.endsWith("_proc") || s.endsWith("_reac"))) {
                expertiseProcReacList.addElement(s);
            }
        }
        if (expertiseProcReacList.size() > 0)
        {
            utils.setScriptVar(player, "expertiseProcReacList", expertiseProcReacList);
        }
        else 
        {
            if (utils.hasScriptVar(player, "expertiseProcReacList"))
            {
                utils.removeScriptVar(player, "expertiseProcReacList");
            }
        }
        proc.buildCurrentProcList(player);
        proc.buildCurrentReacList(player);
    }
    public static void autoAllocateExpertiseByLevel(obj_id player, boolean onLevel) throws InterruptedException
    {
        return;
    }
    public static void displayIntroductionToExpertise(obj_id player) throws InterruptedException
    {
        String title = utils.packStringId(SID_SUI_EXPERTISE_INTRODUCTION_TITLE);
        String prompt = utils.packStringId(SID_SUI_EXPERTISE_INTRODUCTION_BODY);
        int pid = sui.msgbox(player, player, prompt, sui.OK_ONLY, title, "introToExpertise");
        if (pid > -1)
        {
            sui.setAutosaveProperty(pid, false);
            sui.setSizeProperty(pid, 400, 225);
            sui.setLocationProperty(pid, 200, 200);
            flushSUIPage(pid);
        }
    }
    public static boolean hasExpertiseAllocated(obj_id player) throws InterruptedException
    {
        String[] skillList = getSkillListingForPlayer(player);
        if (skillList != null)
        {
            for (String s : skillList) {
                if (s.startsWith("expertise_")) {
                    return true;
                }
            }
        }
        return false;
    }
    public static String[] getExpertiseAllocation(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return null;
        }
        if (!hasExpertiseAllocated(player))
        {
            return null;
        }
        String[] skillList = getSkillListingForPlayer(player);
        Vector resizeExpertiseAllocated = new Vector();
        resizeExpertiseAllocated.setSize(0);
        if (skillList != null)
        {
            for (String s : skillList) {
                if (s.startsWith("expertise_")) {
                    resizeExpertiseAllocated = utils.addElement(resizeExpertiseAllocated, s);
                }
            }
        }
        String[] finalExpertiseAllocated = new String[resizeExpertiseAllocated.size()];
        resizeExpertiseAllocated.toArray(finalExpertiseAllocated);
        return finalExpertiseAllocated;
    }
    public static boolean isProfAllowedSkill(obj_id player, String skillName) throws InterruptedException
    {
        return false;
    }
}
