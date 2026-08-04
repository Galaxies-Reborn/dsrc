package script.library;

import script.*;

import java.util.StringTokenizer;
import java.util.Vector;

public class faction_perk extends script.base_script
{
    public faction_perk()
    {
    }
    public static final String TBL_PREJUDICE = "datatables/faction/prejudice.iff";
    public static final String TBL_PERK_INVENTORY_BASE = "datatables/npc/faction_recruiter/perk_inventory/";
    public static final String PRECU_CATEGORY_FURNITURE = "furniture";
    public static final String PRECU_CATEGORY_WEAPONS_ARMOR = "weapons_armor";
    public static final String PRECU_CATEGORY_INSTALLATIONS = "installation";
    public static final String PRECU_CATEGORY_UNIFORMS = "uniform";
    public static final String PRECU_CATEGORY_HIRELINGS = "hireling";
    public static final String PRECU_CATEGORY_SCHEMATICS = "schematic";
    public static final int PRECU_COMM_LINK_MIN_RANK = 7;
    public static final int PRECU_COMM_LINK_MAX_TEMPLATE_RANK = 12;
    public static final String VAR_COVERT_DETECTOR = "covert_detector";
    public static final String VAR_COVERT_DETECTOR_FACTION = VAR_COVERT_DETECTOR + ".faction";
    public static final String VAR_COVERT_DETECTOR_RANGE = VAR_COVERT_DETECTOR + ".range";
    public static final String VAR_COVERT_DETECTOR_SPEED = VAR_COVERT_DETECTOR + ".speed";
    public static final float BASE_COVERT_DETECT_TIME = 5.0f;
    public static final float DETECTOR_NOTIFY_RANGE = 128.0f;
    public static final string_id PROSE_COVERT_UNCLOAK = new string_id("base_player", "prose_covert_uncloak");
    public static final String VAR_MINEFIELD_BASE = "minefield";
    public static final String VAR_MINEFIELD_SIZE = VAR_MINEFIELD_BASE + ".size";
    public static final String VAR_MINEFIELD_ACTIVE = VAR_MINEFIELD_BASE + ".active";
    public static final String VAR_MINEFIELD_TARGETS = VAR_MINEFIELD_BASE + ".targets";
    public static final String VAR_MINEFIELD_TARGET_IDS = VAR_MINEFIELD_TARGETS + ".ids";
    public static final String VAR_MINEFIELD_TARGET_LOCS = VAR_MINEFIELD_TARGETS + ".locs";
    public static final String VAR_MINEFIELD_EXTENTS_BASE = VAR_MINEFIELD_BASE + ".extents";
    public static final String VAR_MINEFIELD_MIN_X = VAR_MINEFIELD_EXTENTS_BASE + ".minX";
    public static final String VAR_MINEFIELD_MIN_Z = VAR_MINEFIELD_EXTENTS_BASE + ".minZ";
    public static final String VAR_MINEFIELD_MAX_X = VAR_MINEFIELD_EXTENTS_BASE + ".maxX";
    public static final String VAR_MINEFIELD_MAX_Z = VAR_MINEFIELD_EXTENTS_BASE + ".maxZ";
    public static final float BASE_MINEFIELD_TICK = 1.0f;
    public static final int CHANCE_PER_METER = 25;
    public static final float MINE_DAMAGE_RADIUS = 6.0f;
    public static final String STF_PERK = "faction_perk";
    public static final string_id MNU_DEPLOY = new string_id(STF_PERK, "deploy");
    public static final string_id MNU_PACKUP = new string_id(STF_PERK, "packup");
    public static final string_id SID_NO_BUILD_AREA = new string_id(STF_PERK, "no_build_area");
    public static final string_id SID_NO_BUILD_INSIDE = new string_id(STF_PERK, "no_build_inside");
    public static final string_id PROSE_BE_DECLARED = new string_id(STF_PERK, "prose_be_declared");
    public static final string_id PROSE_DECLARED_FATION = new string_id(STF_PERK, "prose_be_declared_faction");
    public static final string_id PROSE_MUST_HAVE_SKILL = new string_id(STF_PERK, "prose_must_have_skill");
    public static final string_id PROSE_NSF_LOTS = new string_id(STF_PERK, "prose_nsf_lots");
    public static final string_id PROSE_WRONG_FACTION = new string_id(STF_PERK, "prose_wrong_faction");
    public static final string_id PROSE_NOT_NEUTRAL = new string_id(STF_PERK, "prose_not_neutral");
    public static final string_id[] FACTION_PERK_GROUPS = 
    {
        new string_id("faction_recruiter", "option_purchase_weapons_armor"),
        new string_id("faction_recruiter", "option_purchase_installation")
    };
    public static final float FACTION_LOSING_COST_MODIFIER = 0.70f;
    public static final String VAR_FACTION = "faction_recruiter.faction";
    public static final String SCRIPT_FACTION_RECRUITER = "npc.faction_recruiter.faction_recruiter";
    public static final String SCRIPT_PLAYER_RECRUITER = "npc.faction_recruiter.player_recruiter";
    public static final String SCRIPT_FACTION_ITEM = "npc.faction_recruiter.faction_item";
    public static final String VAR_TRAINING_SELECTION = "faction_recruiter.training_selection";
    public static final String VAR_TRAINING_COST = "faction_recruiter.cost";
    public static final String VAR_TRAINING_XP = "faction_recruiter.xp";
    public static final String VAR_DECLARED = "faction_recruiter.declared";
    public static final String VAR_FACTION_HIRELING = "faction_recruiter.faction_hireling";
    public static final String VAR_PLAYER = "faction_recruiter.player";
    public static final String VAR_BIO_LINK_FACTION_POINTS = "biolink.faction_points";
    public static final string_id SID_NO_ITEMS_AVAILABLE = new string_id("faction_recruiter", "no_items_available");
    public static final string_id SID_RESIGN_COMPLETE = new string_id("faction_recruiter", "resign_complete");
    public static final string_id SID_COVERT_COMPLETE = new string_id("faction_recruiter", "covert_complete");
    public static final string_id SID_INVALID_AMOUNT_ENTERED = new string_id("faction_recruiter", "invalid_amount_entered");
    public static final string_id SID_NOT_ENOUGH_STANDING_SPEND = new string_id("faction_recruiter", "not_enough_standing_spend");
    public static final string_id SID_NOT_ENOUGH_CREDITS = new string_id("faction_recruiter", "not_enough_credits");
    public static final string_id SID_AMOUNT_TOO_SMALL = new string_id("faction_recruiter", "amount_to_spend_too_small");
    public static final string_id SID_EXPERIENCE_GRANTED = new string_id("faction_recruiter", "training_experience_granted");
    public static final string_id SID_ITEM_PURCHASED = new string_id("faction_recruiter", "item_purchase_complete");
    public static final string_id SID_ACQUIRE_HIRELING = new string_id("faction_recruiter", "hireling_purchase_complete");
    public static final string_id SID_TOO_MANY_HIRELINGS = new string_id("faction_recruiter", "too_many_hirelings");
    public static final string_id SID_HIRELING_RELEASED = new string_id("faction_recruiter", "hireling_released");
    public static final string_id SID_INVENTORY_FULL = new string_id("faction_recruiter", "inventory_full");
    public static final string_id SID_DATAPAD_FULL = new string_id("faction_recruiter", "datapad_full");
    public static final string_id SID_ORDER_PURCHASED = new string_id("faction_recruiter", "order_purchase_complete");
    public static final string_id SID_SCHEMATIC_PURCHASED = new string_id("faction_recruiter", "schematic_purchase_complete");
    public static final string_id SID_SCHEMATIC_DUPLICATE = new string_id("faction_recruiter", "schematic_duplicate");
    public static final int FACTION_NONE = -1;
    public static final int FACTION_REBEL = 0;
    public static final int FACTION_IMPERIAL = 1;
    public static final String COMM_COOLDOWN = "pvp_gcw_comlink.cooldown";
    public static final int COMM_REUSE = 900;
    public static final string_id SID_ALREADY_HAVE = new string_id("gcw", "comm_already_used");
    public static final string_id SID_TOO_LOW_LEVEL = new string_id("gcw", "player_too_low");
    public static final string_id SID_INDOORS = new string_id("gcw", "player_is_indoors");
    public static int prejudicePerkCost(obj_id player, String faction, int base_cost) throws InterruptedException
    {
        if (!isIdValid(player) || (faction == null) || (faction.equals("")) || (base_cost < 1))
        {
            return -1;
        }
        int species = getSpecies(player);
        float mod = getFactionPrejudice(species, faction);
        if (mod > 1.05f)
        {
            mod = 1.05f;
        }
        if (mod > 0)
        {
            float cost = base_cost * mod;
            return (int)cost;
        }
        return -1;
    }
    public static float getFactionPrejudice(int species, String faction) throws InterruptedException
    {
        faction = toLower(faction);
        String strSpecies = utils.getPlayerSpeciesName(species);
        return dataTableGetFloat(TBL_PREJUDICE, strSpecies, faction);
    }
    public static boolean canDeployFactionalDeed(obj_id player, obj_id deed) throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(deed))
        {
            return false;
        }
        if (getOwner(deed) != player)
        {
            LOG("LOG_CHANNEL", "canDeployFactionalDeed: player != deed owner");
            return false;
        }
        String pFac = factions.getFaction(player);
        int pvpType = pvpGetType(player);
        if (pvpType == PVPTYPE_NEUTRAL)
        {
            prose_package ppBeDeclared = prose.getPackage(PROSE_NOT_NEUTRAL, deed);
            sendSystemMessageProse(player, ppBeDeclared);
            return false;
        }
        String dFac = factions.getFaction(deed);
        if (dFac != null && !dFac.equals(""))
        {
            if (!pFac.equals(dFac))
            {
                prose_package ppWrongFaction = prose.getPackage(PROSE_WRONG_FACTION, deed, dFac);
                sendSystemMessageProse(player, ppWrongFaction);
                return false;
            }
        }
        String template = player_structure.getDeedTemplate(deed);
        if ((template == null) || (template.equals("")))
        {
            LOG("LOG_CHANNEL", "canDeployFactionalDeed: bad deed template!!");
            return false;
        }
        if (!gcw.canPlaceFactionBaseByPlanet(player, deed, getLocation(player).area))
        {
            sendSystemMessage(player, new string_id("gcw", "cannot_place_additional_base"));
            return false;
        }
        int used = getIntObjVar(player, player_structure.VAR_LOTS_USED);
        String fp_template = player_structure.getFootprintTemplate(template);
        if ((fp_template == null) || (fp_template.equals("")))
        {
            fp_template = template;
        }
        location here = getLocation(player);
        if (isIdValid(here.cell))
        {
            sendSystemMessage(player, SID_NO_BUILD_AREA);
            return false;
        }
        region[] rgnTest = getRegionsWithBuildableAtPoint(here, regions.BUILD_FALSE);
        if (rgnTest != null)
        {
            sendSystemMessage(player, SID_NO_BUILD_AREA);
            return false;
        }
        if (!player_structure.canPlaceFactionPerkDeed(deed, player))
        {
            return false;
        }
        return true;
    }
    public static void decloakCovertFactionMember(obj_id detector, obj_id player) throws InterruptedException
    {
        if (!isIdValid(detector) || !isIdValid(player))
        {
            return;
        }
        if (isPlayer(player))
        {
            int dFac = pvpGetAlignedFaction(detector);
            int pFac = pvpGetAlignedFaction(player);
            if (pvpAreFactionsOpposed(dFac, pFac) && !factions.isDeclared(player))
            {
                String pFacName = factions.getFaction(player);
                String dFacName = factions.getFactionNameByHashCode(dFac);
                boolean hasGlobalTef = false;
                String[] tefs = pvpGetEnemyFlags(player);
                if (tefs != null && tefs.length > 0)
                {
                    for (String tef : tefs) {
                        StringTokenizer st = new StringTokenizer(tef);
                        String sTarget = st.nextToken();
                        String sTefFac = st.nextToken();
                        String sExpiration = st.nextToken();
                        int iTefFac = utils.stringToInt(sTefFac);
                        String tefFac = factions.getFactionNameByHashCode(iTefFac);
                        if (tefFac != null && !tefFac.equals("")) {
                            if (tefFac.equals(dFacName)) {
                                obj_id target = utils.stringToObjId(sTarget);
                                if (!isIdValid(target)) {
                                    hasGlobalTef = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (hasGlobalTef)
                {
                    return;
                }
                string_id sidPlayerFactionName = factions.getLocalizedFactionName(pFacName);
                prose_package ppUncloak = prose.getPackage(PROSE_COVERT_UNCLOAK, detector, sidPlayerFactionName);
                sendSystemMessageProse(player, ppUncloak);
                if (!factions.setTemporaryEnemyFlag(player, detector))
                {
                }
                else 
                {
                    obj_id[] inRange = getNonCreaturesInRange(detector, DETECTOR_NOTIFY_RANGE);
                    if ((inRange != null) && (inRange.length > 0))
                    {
                        dictionary d = new dictionary();
                        d.put("target", player);
                        for (obj_id obj_id : inRange) {
                            if (getGameObjectType(obj_id) == GOT_installation_turret) {
                                messageTo(obj_id, "enemyDecloaked", d, 3, false);
                            }
                        }
                    }
                }
            }
        }
    }
    public static boolean displayAvailableFactionItemRanks(obj_id player, obj_id npc, int playerGcwRank, String playerGcwFaction) throws InterruptedException
    {
        if (!isValidPrecuFactionPurchase(player, playerGcwFaction) || !isIdValid(npc))
        {
            return false;
        }
        String scriptvar_path = "recruiter.item_rank." + player;
        if (utils.hasScriptVar(npc, scriptvar_path + ".pid"))
        {
            sui.closeSUI(player, utils.getIntScriptVar(npc, scriptvar_path + ".pid"));
        }
        utils.removeScriptVar(npc, scriptvar_path + ".pid");
        utils.removeBatchScriptVar(npc, scriptvar_path + ".categories");
        Vector categoryKeys = new Vector();
        categoryKeys.setSize(0);
        Vector categoryNames = new Vector();
        categoryNames.setSize(0);
        String[] candidates = 
        {
            PRECU_CATEGORY_FURNITURE,
            PRECU_CATEGORY_WEAPONS_ARMOR,
            PRECU_CATEGORY_SCHEMATICS,
            PRECU_CATEGORY_INSTALLATIONS,
            PRECU_CATEGORY_UNIFORMS,
            PRECU_CATEGORY_HIRELINGS
        };
        boolean declared = factions.isDeclared(player);
        for (String category : candidates)
        {
            if ((category.equals(PRECU_CATEGORY_INSTALLATIONS) || category.equals(PRECU_CATEGORY_UNIFORMS) || category.equals(PRECU_CATEGORY_HIRELINGS)) && !declared)
            {
                continue;
            }
            if (category.equals(PRECU_CATEGORY_UNIFORMS) && !toLower(playerGcwFaction).equals("imperial"))
            {
                continue;
            }
            if (hasAvailablePrecuFactionItems(player, playerGcwFaction, category))
            {
                categoryKeys = utils.addElement(categoryKeys, category);
                categoryNames = utils.addElement(categoryNames, getPrecuCategoryTitle(category));
            }
        }
        if (categoryKeys == null || categoryKeys.size() == 0)
        {
            sendSystemMessage(player, SID_NO_ITEMS_AVAILABLE);
            return false;
        }
        String[] storedCategories = new String[categoryKeys.size()];
        categoryKeys.toArray(storedCategories);
        int pid = sui.listbox(npc, player, "@faction_recruiter:select_item_purchase", sui.OK_CANCEL, "@faction_recruiter:faction_purchase", categoryNames, "msgFactionItemRankSelected");
        if (pid < 0)
        {
            return false;
        }
        utils.setScriptVar(npc, scriptvar_path + ".pid", pid);
        utils.setBatchScriptVar(npc, scriptvar_path + ".categories", storedCategories);
        return true;
    }

    public static boolean isValidPrecuFactionPurchase(obj_id player, String faction) throws InterruptedException
    {
        if (!isIdValid(player) || faction == null || faction.equals("") || pvpGetType(player) == PVPTYPE_NEUTRAL)
        {
            return false;
        }
        String alignedFaction = factions.getFactionNameByHashCode(pvpGetAlignedFaction(player));
        return alignedFaction != null && toLower(alignedFaction).equals(toLower(faction)) && (toLower(faction).equals("rebel") || toLower(faction).equals("imperial"));
    }

    public static String[] getPrecuCategoryTables(String faction, String category) throws InterruptedException
    {
        String base = TBL_PERK_INVENTORY_BASE + toLower(faction) + "/";
        if (category.equals(PRECU_CATEGORY_FURNITURE))
        {
            return new String[] { base + "furniture.iff" };
        }
        if (category.equals(PRECU_CATEGORY_WEAPONS_ARMOR))
        {
            return new String[] { base + "weapon.iff" };
        }
        if (category.equals(PRECU_CATEGORY_INSTALLATIONS))
        {
            return new String[] { base + "installation.iff" };
        }
        if (category.equals(PRECU_CATEGORY_UNIFORMS))
        {
            return new String[] { base + "uniform.iff" };
        }
        if (category.equals(PRECU_CATEGORY_HIRELINGS))
        {
            return new String[] { base + "hireling.iff" };
        }
        if (category.equals(PRECU_CATEGORY_SCHEMATICS))
        {
            return new String[] { base + "schematic.iff" };
        }
        return new String[0];
    }

    public static String getPrecuCategoryTitle(String category) throws InterruptedException
    {
        if (category.equals(PRECU_CATEGORY_FURNITURE))
        {
            return "@faction_recruiter:option_purchase_furniture";
        }
        if (category.equals(PRECU_CATEGORY_WEAPONS_ARMOR))
        {
            return "@faction_recruiter:option_purchase_weapons_armor";
        }
        if (category.equals(PRECU_CATEGORY_INSTALLATIONS))
        {
            return "@faction_recruiter:option_purchase_installation";
        }
        if (category.equals(PRECU_CATEGORY_UNIFORMS))
        {
            return "@faction_recruiter:option_purchase_uniforms";
        }
        if (category.equals(PRECU_CATEGORY_HIRELINGS))
        {
            return "@faction_recruiter:option_hirelings";
        }
        return "@faction_recruiter:option_purchase_schematics";
    }

    public static boolean hasAvailablePrecuFactionItems(obj_id player, String faction, String category) throws InterruptedException
    {
        String[] tables = getPrecuCategoryTables(faction, category);
        for (String table : tables)
        {
            int count = dataTableGetNumRows(table);
            for (int rowIndex = 0; rowIndex < count; rowIndex++)
            {
                dictionary row = dataTableGetRow(table, rowIndex);
                if (isAvailablePrecuFactionItem(player, row))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAvailablePrecuFactionItem(obj_id player, dictionary row) throws InterruptedException
    {
        if (row == null || row.isEmpty())
        {
            return false;
        }
        String template = row.getString("template");
        if (template == null || template.equals("") || row.getInt("cost") < 1)
        {
            return false;
        }
        if (row.getInt("declared") == 1 && !factions.isDeclared(player))
        {
            return false;
        }
        return !template.startsWith("object/draft_schematic") || !hasSchematic(player, template);
    }

    public static String getPrecuFactionItemName(dictionary row) throws InterruptedException
    {
        String name = row.getString("name");
        String template = row.getString("template");
        if ((name == null || name.equals("")) && template.startsWith("object/draft_schematic"))
        {
            string_id nameId = getProductNameFromSchematic(template);
            if (nameId != null)
            {
                name = "@" + nameId;
            }
        }
        if (name == null || name.equals(""))
        {
            string_id nameId = getNameFromTemplate(template);
            if (nameId != null)
            {
                name = "@" + nameId;
            }
        }
        return (name == null || name.equals("")) ? template : name;
    }

    public static boolean displayItemPurchaseSUI(obj_id player, int ignoredRank, String faction, obj_id objNPC) throws InterruptedException
    {
        return displayAvailableFactionItemRanks(player, isIdValid(objNPC) ? objNPC : getSelf(), pvpGetCurrentGcwRank(player), faction);
    }
    public static boolean displayItemPurchaseSUI(obj_id player, int ignoredRank, String faction) throws InterruptedException
    {
        return displayAvailableFactionItemRanks(player, getSelf(), pvpGetCurrentGcwRank(player), faction);
    }
    public static boolean displayItemPurchaseSUI(obj_id player, int ignoredRank, String faction, float ignoredMultiplier) throws InterruptedException
    {
        return displayAvailableFactionItemRanks(player, getSelf(), pvpGetCurrentGcwRank(player), faction);
    }
    public static boolean displayItemPurchaseSUI(obj_id player, int ignoredRank, String faction, float ignoredMultiplier, obj_id objNPC) throws InterruptedException
    {
        return displayAvailableFactionItemRanks(player, isIdValid(objNPC) ? objNPC : getSelf(), pvpGetCurrentGcwRank(player), faction);
    }
    public static boolean displayItemPurchaseSUI(obj_id player, String category, String faction, obj_id objRecruiter) throws InterruptedException
    {
        obj_id self = isIdValid(objRecruiter) ? objRecruiter : getSelf();
        if (!isValidPrecuFactionPurchase(player, faction))
        {
            return false;
        }
        String[] tables = getPrecuCategoryTables(faction, category);
        if (tables.length == 0)
        {
            return false;
        }
        String scriptvar_path = "recruiter.item_purchase." + player;
        if (utils.hasScriptVar(self, scriptvar_path + ".pid"))
        {
            sui.closeSUI(player, utils.getIntScriptVar(self, scriptvar_path + ".pid"));
        }
        utils.removeScriptVar(self, scriptvar_path + ".pid");
        utils.removeBatchScriptVar(self, scriptvar_path + ".template");
        utils.removeBatchScriptVar(self, scriptvar_path + ".table");
        utils.removeBatchScriptVar(self, scriptvar_path + ".item_names");
        utils.removeScriptVar(self, scriptvar_path + ".category");
        utils.removeScriptVar(self, scriptvar_path + ".faction");
        Vector items = new Vector();
        items.setSize(0);
        Vector templates = new Vector();
        templates.setSize(0);
        Vector itemTables = new Vector();
        itemTables.setSize(0);
        for (String table : tables)
        {
            int num_items = dataTableGetNumRows(table);
            for (int i = 0; i < num_items; i++)
            {
                dictionary row = dataTableGetRow(table, i);
                if (isAvailablePrecuFactionItem(player, row))
                {
                    String row_template = row.getString("template");
                    String row_name = getPrecuFactionItemName(row);
                    int cost = prejudicePerkCost(player, toLower(faction), row.getInt("cost"));
                    if (cost < 1)
                    {
                        cost = row.getInt("cost");
                    }
                    items = utils.addElement(items, row_name + " (Faction Points: " + cost + ")");
                    templates = utils.addElement(templates, row_template);
                    itemTables = utils.addElement(itemTables, table);
                }
            }
        }
        if (items == null || items.size() < 1)
        {
            sendSystemMessage(player, faction_perk.SID_NO_ITEMS_AVAILABLE);
            faction_perk.displayAvailableFactionItemRanks(player, self, pvpGetCurrentGcwRank(player), faction);
            return false;
        }
        String[] storedTemplates = new String[templates.size()];
        templates.toArray(storedTemplates);
        String[] storedTables = new String[itemTables.size()];
        itemTables.toArray(storedTables);
        String[] storedNames = new String[items.size()];
        items.toArray(storedNames);
        int pid = sui.listbox(self, player, "@faction_recruiter:select_item_purchase", sui.OK_CANCEL_REFRESH, getPrecuCategoryTitle(category), items, "msgFactionItemPurchaseSelected", false, false);
        if (pid > -1)
        {
            sui.listboxUseOtherButton(pid, "Back");
            sui.showSUIPage(pid);
            utils.setScriptVar(self, scriptvar_path + ".pid", pid);
            utils.setBatchScriptVar(self, scriptvar_path + ".template", storedTemplates);
            utils.setBatchScriptVar(self, scriptvar_path + ".table", storedTables);
            utils.setBatchScriptVar(self, scriptvar_path + ".item_names", storedNames);
            utils.setScriptVar(self, scriptvar_path + ".category", category);
            utils.setScriptVar(self, scriptvar_path + ".faction", faction);
            return true;
        }
        return false;
    }
    public static float getModifiedGCWCost(float fltCost, obj_id objNPC, String strFaction) throws InterruptedException
    {
        float MINIMUM_MODIFIER = 0.50f;
        float MAXIMUM_MODIFIER = 1.50f;
        float fltRatio = 0.0f;
        if (strFaction.equals("Imperial"))
        {
            fltRatio = gcw.getRebelRatio(objNPC);
        }
        else if (strFaction.equals("Rebel"))
        {
            fltRatio = gcw.getImperialRatio(objNPC);
        }
        else 
        {
            return fltCost;
        }
        LOG("gcw", "Ratio is " + fltRatio);
        float fltModifier = MINIMUM_MODIFIER + fltRatio;
        if (fltModifier > MAXIMUM_MODIFIER)
        {
            fltModifier = MAXIMUM_MODIFIER;
        }
        LOG("gcw", "Modifier is " + fltModifier);
        fltCost = fltCost * fltModifier;
        return fltCost;
    }
    public static void factionItemPurchased(dictionary params) throws InterruptedException
    {
        factionItemPurchased(params, 1.0f);
    }
    public static void factionItemPurchased(dictionary params, float systemMultiplier) throws InterruptedException
    {
        obj_id self = getSelf();
        if (params == null || params.isEmpty())
        {
            return;
        }
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
        {
            return;
        }
        String scriptvar_path = "recruiter.item_purchase." + player;
        if (!utils.hasScriptVar(self, scriptvar_path + ".pid"))
        {
            return;
        }
        int oldPid = utils.getIntScriptVar(self, scriptvar_path + ".pid");
        String[] available_items = utils.getStringBatchScriptVar(self, scriptvar_path + ".template");
        String[] available_tables = utils.getStringBatchScriptVar(self, scriptvar_path + ".table");
        String[] item_names = utils.getStringBatchScriptVar(self, scriptvar_path + ".item_names");
        String category = utils.getStringScriptVar(self, scriptvar_path + ".category");
        String faction = utils.getStringScriptVar(self, scriptvar_path + ".faction");
        utils.removeScriptVar(self, scriptvar_path + ".pid");
        utils.removeBatchScriptVar(self, scriptvar_path + ".template");
        utils.removeBatchScriptVar(self, scriptvar_path + ".table");
        utils.removeBatchScriptVar(self, scriptvar_path + ".item_names");
        utils.removeScriptVar(self, scriptvar_path + ".category");
        utils.removeScriptVar(self, scriptvar_path + ".faction");
        if (available_items == null || available_tables == null || item_names == null || available_items.length == 0 || available_items.length != available_tables.length || available_items.length != item_names.length)
        {
            LOG("LOG_CHANNEL", "faction_recruiter::msgFactionItemPurchaseSelected -- the item template list is null.");
            return;
        }
        if (!isValidPrecuFactionPurchase(player, faction))
        {
            return;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return;
        }
        if (button == sui.BP_REVERT)
        {
            faction_perk.displayAvailableFactionItemRanks(player, self, pvpGetCurrentGcwRank(player), faction);
            return;
        }
        int row_selected = sui.getListboxSelectedRow(params);
        if (row_selected < 0)
        {
            return;
        }
        if (row_selected >= available_items.length)
        {
            return;
        }
        String item_template = available_items[row_selected];
        String perksDatatable = available_tables[row_selected];
        if (item_template == null)
        {
            LOG("LOG_CHANNEL", "faction_recruiter::msgFactionItemPurchaseSelected -- the item template selected by " + self + " is null.");
            return;
        }
        if (!isPrecuCategoryTable(perksDatatable, faction, category))
        {
            return;
        }
        int idx = dataTableSearchColumnForString(item_template, "template", perksDatatable);
        if (idx == -1)
        {
            LOG("LOG_CHANNEL", "faction_recruiter::msgFactionItemPurchaseSelected -- cannot find " + item_template + " in the perk inventory datatable.");
            return;
        }
        dictionary row = dataTableGetRow(perksDatatable, idx);
        if (!isAvailablePrecuFactionItem(player, row))
        {
            return;
        }
        String name = row.getString("name");
        int base_cost = row.getInt("cost");
        int declared = row.getInt("declared");
        int cost = faction_perk.prejudicePerkCost(player, faction, base_cost);
        if (cost < 0)
        {
            cost = base_cost;
        }
        float standing = factions.getFactionStanding(player, faction);
        if (standing < cost + factions.FACTION_RATING_DECLARABLE_MIN)
        {
            prose_package pp = prose.getPackage(faction_perk.SID_NOT_ENOUGH_STANDING_SPEND);
            prose.setDI(pp, (int)factions.FACTION_RATING_DECLARABLE_MIN);
            prose.setTO(pp, faction);
            sendSystemMessageProse(player, pp);
            return;
        }
        boolean granted = false;
        boolean temporarySchematic = false;
        obj_id grantedObject = obj_id.NULL_ID;
        if (category.equals(PRECU_CATEGORY_SCHEMATICS))
        {
            if (hasSchematic(player, item_template))
            {
                sendSystemMessage(player, faction_perk.SID_SCHEMATIC_DUPLICATE);
                return;
            }
            int uses = row.getInt("uses");
            temporarySchematic = uses > 0;
            granted = temporarySchematic ? temp_schematic.grant(player, item_template, uses) : grantSchematic(player, item_template);
        }
        else if (category.equals(PRECU_CATEGORY_HIRELINGS))
        {
            obj_id datapad = utils.getPlayerDatapad(player);
            if (!isIdValid(datapad) || getVolumeFree(datapad) < 1)
            {
                sendSystemMessage(player, faction_perk.SID_DATAPAD_FULL);
                return;
            }
            if (pet_lib.hasMaxStoredPetsOfType(player, pet_lib.PET_TYPE_NPC))
            {
                sendSystemMessage(player, faction_perk.SID_TOO_MANY_HIRELINGS);
                return;
            }
            grantedObject = createObject(pet_lib.PET_CTRL_DEVICE_TEMPLATE, datapad, "");
            if (isIdValid(grantedObject))
            {
                setObjVar(grantedObject, "pet.creatureName", item_template);
                setObjVar(grantedObject, "ai.pet.type", pet_lib.PET_TYPE_NPC);
                setObjVar(grantedObject, faction_perk.VAR_FACTION, faction);
                setObjVar(grantedObject, faction_perk.VAR_FACTION_HIRELING, 1);
                setName(grantedObject, new string_id("mob/creature_names", item_template));
                attachScript(grantedObject, "ai.pet_control_device");
                granted = true;
            }
        }
        else
        {
            obj_id inv = getObjectInSlot(player, "inventory");
            if (!isIdValid(inv) || getVolumeFree(inv) < 1)
            {
                sendSystemMessage(player, faction_perk.SID_INVENTORY_FULL);
                return;
            }
            grantedObject = weapons.createPossibleWeapon(item_template, inv, 0.8f);
            if (isIdValid(grantedObject))
            {
                setObjVar(grantedObject, faction_perk.VAR_FACTION, faction);
                if (declared == 1)
                {
                    setObjVar(grantedObject, faction_perk.VAR_DECLARED, 1);
                }
                attachScript(grantedObject, faction_perk.SCRIPT_FACTION_ITEM);
                granted = true;
            }
        }
        if (!granted)
        {
            CustomerServiceLog("faction_perk", "(" + player + ")" + getName(player) + " failed to create PRE-CU faction perk " + item_template);
            return;
        }
        if (!factions.addUnmodifiedFactionStanding(player, faction, -cost, false))
        {
            if (category.equals(PRECU_CATEGORY_SCHEMATICS))
            {
                if (temporarySchematic)
                {
                    temp_schematic.revoke(player, item_template);
                }
                else
                {
                    revokeSchematic(player, item_template);
                }
            }
            else if (isIdValid(grantedObject))
            {
                destroyObject(grantedObject);
            }
            return;
        }
        CustomerServiceLog("faction_perk", "(" + player + ")" + getName(player) + " purchased PRE-CU faction perk " + item_template + " for " + cost + " faction points");
        logBalance("precuFactionPerkPurchase;" + getGameTime() + ";" + faction + ";" + category + ";" + item_template + ";" + cost);
        if (category.equals(PRECU_CATEGORY_SCHEMATICS))
        {
            sendSystemMessage(player, faction_perk.SID_SCHEMATIC_PURCHASED);
        }
        else if (category.equals(PRECU_CATEGORY_HIRELINGS))
        {
            prose_package pp = prose.getPackage(faction_perk.SID_ACQUIRE_HIRELING);
            prose.setTT(pp, new string_id("mob/creature_names", item_template));
            sendSystemMessageProse(player, pp);
        }
        else
        {
            prose_package pp = prose.getPackage(faction_perk.SID_ITEM_PURCHASED, player, grantedObject);
            sendSystemMessageProse(player, pp);
        }
        faction_perk.displayItemPurchaseSUI(player, category, faction, self);
    }

    public static boolean isPrecuCategoryTable(String table, String faction, String category) throws InterruptedException
    {
        if (table == null || faction == null || category == null)
        {
            return false;
        }
        String[] expectedTables = getPrecuCategoryTables(faction, category);
        for (String expected : expectedTables)
        {
            if (expected.equals(table))
            {
                return true;
            }
        }
        return false;
    }
    public static void applyFactionCostObjvarFromSchematic(obj_id craftedObject, obj_id manfSchematic) throws InterruptedException
    {
        if (!isIdValid(craftedObject) || !isIdValid(manfSchematic))
        {
            CustomerServiceLog("Faction", "WARNING: faction_perk.applyFactionCostObjvarFromSchematic called with invalid object " + craftedObject + " or schematic " + manfSchematic);
            return;
        }
        String draftSchematic = getDraftSchematic(manfSchematic);
        if (draftSchematic == null || draftSchematic.length() == 0)
        {
            CustomerServiceLog("Faction", "WARNING: faction_perk.applyFactionCostObjvarFromSchematic could not " + "find draft schematic template from manf schematic " + manfSchematic);
            return;
        }
        obj_id player = getCrafter(craftedObject);
        if (!isIdValid(player))
        {
            CustomerServiceLog("Faction", "WARNING: faction_perk.applyFactionCostObjvarFromSchematic could not " + "find owner of crafted object " + craftedObject);
            return;
        }
        String factionName = getStringObjVar(craftedObject, VAR_FACTION);
        if (factionName == null || factionName.length() == 0)
        {
            CustomerServiceLog("Faction", "WARNING: faction_perk.applyFactionCostObjvarFromSchematic could not " + "get faction name from objvar " + VAR_FACTION + " on object ", craftedObject);
            return;
        }
        String tbl = TBL_PERK_INVENTORY_BASE + toLower(factionName) + "/schematic.iff";
        int schematicRow = dataTableSearchColumnForString(draftSchematic, "template", tbl);
        if (schematicRow < 0)
        {
            CustomerServiceLog("Faction", "WARNING: faction_perk.applyFactionCostObjvarFromSchematic could not " + "find draft schematic template " + draftSchematic + " in datatable " + tbl);
            return;
        }
        int cost = dataTableGetInt(tbl, schematicRow, "owner_cost");
        if (cost > 0)
        {
            setObjVar(craftedObject, VAR_BIO_LINK_FACTION_POINTS, cost);
        }
    }
    public static boolean isValidReconTarget(obj_id player, obj_id target) throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(target))
        {
            return false;
        }
        int pFac = pvpGetAlignedFaction(player);
        int tFac = pvpGetAlignedFaction(target);
        if (isPlayer(player) && pvpGetType(player) == PVPTYPE_NEUTRAL)
        {
            pFac = 0;
        }
        if (isPlayer(target) && pvpGetType(target) == PVPTYPE_NEUTRAL)
        {
            tFac = 0;
        }
        if (!pvpAreFactionsOpposed(pFac, tFac))
        {
            return false;
        }
        int got = getGameObjectType(target);
        if (isGameObjectTypeOf(got, GOT_installation_turret))
        {
            return true;
        }
        else if (isGameObjectTypeOf(got, GOT_installation_minefield))
        {
            return true;
        }
        else if (isGameObjectTypeOf(got, GOT_building_factional))
        {
            return true;
        }
        return false;
    }
    public static String[] getReconReport(obj_id player, obj_id target) throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(target))
        {
            return null;
        }
        if (!isValidReconTarget(player, target))
        {
            return null;
        }
        Vector dta = new Vector();
        dta.setSize(0);
        int got = getGameObjectType(target);
        if (isGameObjectTypeOf(got, GOT_installation_turret))
        {
        }
        else if (isGameObjectTypeOf(got, GOT_building_factional))
        {
        }
        if (hasScript(target, "faction_perk.minefield.field"))
        {
        }
        String[] _dta = new String[0];
        if (dta != null)
        {
            _dta = new String[dta.size()];
            dta.toArray(_dta);
        }
        return _dta;
    }
    public static void giveBonusBaseDeeds(obj_id player, obj_id inv, String item_template, String faction, int declared) throws InterruptedException
    {
        int level = 1;
        if (item_template.contains("object/tangible/deed/faction_perk/hq/hq_s03"))
        {
            level = 2;
        }
        else if (item_template.contains("object/tangible/deed/faction_perk/hq/hq_s04"))
        {
            level = 3;
        }
        int level2 = 1;
        if (level == 3)
        {
            level2 = 2;
        }
        String item_template1 = "object/tangible/deed/faction_perk/hq/hq_s0" + level + "_" + toLower(faction) + ".iff";
        String item_template2 = "object/tangible/deed/faction_perk/hq/hq_s0" + level2 + "_" + toLower(faction) + ".iff";
        obj_id item1 = createObjectInInventoryAllowOverload(item_template1, player);
        setObjVar(item1, VAR_FACTION, faction);
        if (declared == 1)
        {
            setObjVar(item1, VAR_DECLARED, 1);
        }
        attachScript(item1, SCRIPT_FACTION_ITEM);
        obj_id item2 = createObjectInInventoryAllowOverload(item_template2, player);
        setObjVar(item2, VAR_FACTION, faction);
        if (declared == 1)
        {
            setObjVar(item2, VAR_DECLARED, 1);
        }
        attachScript(item2, SCRIPT_FACTION_ITEM);
        string_id PROSE_BONUS_BASE = new string_id(STF_PERK, "bonus_base_name");
        string_id strSpam = new string_id("faction_perk", "given_extra_bases");
        prose_package spam1 = prose.getPackage(PROSE_BONUS_BASE, getEncodedName(item1));
        prose_package spam2 = prose.getPackage(PROSE_BONUS_BASE, getEncodedName(item2));
        sendSystemMessage(player, strSpam);
        sendSystemMessageProse(player, spam1);
        sendSystemMessageProse(player, spam2);
        return;
    }
    public static int grabFactionBasePointValue(obj_id base) throws InterruptedException
    {
        String base_template = getTemplateName(base);
        int default_point_value = 1;
        final String TBL_HQ_POINT_VALUE = "datatables/faction_perk/hq/hq_point_values.iff";
        int idx = dataTableSearchColumnForString(base_template, "base_type", TBL_HQ_POINT_VALUE);
        if (idx == -1)
        {
            LOG("LOG_CHANNEL", "faction_perk::grabFactionBasePointValue -- cannot find " + base_template + " in the hq_point_values datatable.");
            return default_point_value;
        }
        dictionary row = dataTableGetRow(TBL_HQ_POINT_VALUE, idx);
        int point_value = row.getInt("point_value");
        if (point_value < 1)
        {
            point_value = default_point_value;
        }
        return point_value;
    }
    public static boolean executeComlinkReinforcements(obj_id player) throws InterruptedException
    {
        obj_id comlink = getPlayerComlink(player);
        if (!isIdValid(comlink))
        {
            debugSpeakMsg(player, "no link");
            return false;
        }
        if (utils.hasScriptVar(player, COMM_COOLDOWN))
        {
            int timeElapsed = getGameTime() - utils.getIntScriptVar(player, COMM_COOLDOWN);
            if (timeElapsed < COMM_REUSE)
            {
                sendSystemMessage(player, SID_ALREADY_HAVE);
                return false;
            }
        }
        if (!static_item.validateLevelRequired(player, comlink))
        {
            sendSystemMessage(player, SID_TOO_LOW_LEVEL);
            return false;
        }
        obj_id world = getTopMostContainer(player);
        if (world != player)
        {
            sendSystemMessage(player, SID_INDOORS);
            return false;
        }
        if (!spawnTroopers(player))
        {
            debugSpeakMsg(player, "failz to spawn");
            return false;
        }
        sendCooldownGroupTimingOnly(player, (-1145728732), 900.0f);
        utils.setScriptVar(player, COMM_COOLDOWN, getGameTime());
        return true;
    }
    public static obj_id getPlayerComlink(obj_id player) throws InterruptedException
    {
        if (factions.isRebel(player))
        {
            return utils.getStaticItemInInventory(player, "item_pvp_lieutenant_comm_link_rebel_reward_04_01");
        }
        else if (factions.isImperial(player))
        {
            return utils.getStaticItemInInventory(player, "item_pvp_lieutenant_comm_link_imperial_reward_04_01");
        }
        else 
        {
            return null;
        }
    }
    public static boolean spawnTroopers(obj_id player) throws InterruptedException
    {
        return spawnTroopers(player, null, -1);
    }
    public static boolean spawnTroopers(obj_id player, String faction, int rank) throws InterruptedException
    {
        if (rank == -1)
        {
            rank = pvpGetCurrentGcwRank(player);
        }
        if (faction == null || (!faction.equals("rebel") && !faction.equals("imperial")))
        {
            faction = factions.isRebel(player) ? "rebel" : "imperial";
        }
        if (rank < PRECU_COMM_LINK_MIN_RANK)
        {
            sendSystemMessage(player, new string_id("gcw", "gcw_officer_only_use"));
            return false;
        }
        int reinforcementRank = Math.min(rank, PRECU_COMM_LINK_MAX_TEMPLATE_RANK);
        String toSpawn = "gcw_comm_link_reinforcement_" + faction + "_" + reinforcementRank;
        int atLevel = skill.getPrecuEncounterDifficulty(player);
        obj_id reinforcement = create.object(toSpawn, getLocation(player), atLevel);
        if (!isIdValid(reinforcement))
        {
            return false;
        }
        setMaster(reinforcement, player);
        attachScript(reinforcement, "item.gcw_buff_banner.pvp_lieutenant_comm_link_trooper");
        obj_id[] haters = getWhoIsTargetingMe(player);
        combat.sendCombatSpamMessage(player, new string_id("spam", "reinforcements_arrived"));
        for (int i = 0; haters != null && haters.length > 0 && i < haters.length; i++)
        {
            if (pvpCanAttack(player, haters[i]))
            {
                setHate(reinforcement, haters[i], 1);
            }
        }
        return true;
    }
}
