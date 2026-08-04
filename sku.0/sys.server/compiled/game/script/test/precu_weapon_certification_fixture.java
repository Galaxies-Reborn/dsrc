package script.test;

import script.obj_id;
import script.library.combat;
import script.library.utils;

/**
 * Observation-only ServerConsole fixture for the Publish 14.1 weapon
 * certification seam. It creates two temporary inventory weapons, exercises
 * the production certification method, restores the pre-existing certification
 * cache exactly, and destroys both objects before returning.
 */
public class precu_weapon_certification_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String CDEF_TEMPLATE =
        "object/weapon/ranged/pistol/pistol_cdef.iff";
    private static final String LIGHTSABER_TEMPLATE =
        "object/weapon/melee/sword/crafted_saber/sword_lightsaber_training.iff";
    private static final String CDEF_CERTIFICATION = "cert_pistol_cdef";
    private static final String LIGHTSABER_CERTIFICATION =
        "cert_onehandlightsaber_training";
    private static final String CERTIFICATION_CACHE = "combat.weaponCertified";

    public String executeProbe(String params) throws InterruptedException
    {
        long playerValue;
        try
        {
            playerValue = Long.parseLong(params == null ? "" : params.trim());
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (playerValue != PLAYER_OID)
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player))
        {
            return "error=playerNotAuthoritative";
        }
        if (getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=stationNotAllowed";
        }

        boolean hadCache = utils.hasScriptVar(player, CERTIFICATION_CACHE);
        obj_id originalCache = hadCache ?
            utils.getObjIdScriptVar(player, CERTIFICATION_CACHE) : obj_id.NULL_ID;
        obj_id cdef = obj_id.NULL_ID;
        obj_id lightsaber = obj_id.NULL_ID;
        boolean temporaryCdefCommand = false;
        String result;

        try
        {
            cdef = createObjectInInventoryAllowOverload(CDEF_TEMPLATE, player);
            lightsaber =
                createObjectInInventoryAllowOverload(LIGHTSABER_TEMPLATE, player);
            if (!isIdValid(cdef) || !CDEF_TEMPLATE.equals(getTemplateName(cdef)))
            {
                result = "error=cdefCreationFailed";
            }
            else if (!isIdValid(lightsaber) ||
                !LIGHTSABER_TEMPLATE.equals(getTemplateName(lightsaber)))
            {
                result = "error=lightsaberCreationFailed";
            }
            else
            {
                String[] cdefRequirements = getRequiredCertifications(cdef);
                String[] lightsaberRequirements =
                    getRequiredCertifications(lightsaber);

                boolean cdefOwnedBefore =
                    hasCommand(player, CDEF_CERTIFICATION);
                utils.removeScriptVar(player, CERTIFICATION_CACHE);
                boolean cdefCertifiedBefore =
                    combat.hasCertification(player, cdef, false);

                if (!cdefOwnedBefore)
                {
                    grantCommand(player, CDEF_CERTIFICATION);
                    temporaryCdefCommand = true;
                }
                boolean cdefOwnedDuring =
                    hasCommand(player, CDEF_CERTIFICATION);
                utils.removeScriptVar(player, CERTIFICATION_CACHE);
                boolean cdefCertifiedDuring =
                    combat.hasCertification(player, cdef, false);

                if (temporaryCdefCommand)
                {
                    revokeCommand(player, CDEF_CERTIFICATION);
                    temporaryCdefCommand =
                        hasCommand(player, CDEF_CERTIFICATION);
                }
                boolean cdefOwnedAfter =
                    hasCommand(player, CDEF_CERTIFICATION);
                utils.removeScriptVar(player, CERTIFICATION_CACHE);
                boolean lightsaberCertified =
                    combat.hasCertification(player, lightsaber, false);

                result =
                    "action=probe" +
                    " authoritative=true" +
                    " god=" + isGod(player) +
                    " cdefRequirements=" + joinRequirements(cdefRequirements) +
                    " cdefOwnedBefore=" + cdefOwnedBefore +
                    " cdefCertifiedBefore=" + cdefCertifiedBefore +
                    " cdefOwnedDuring=" + cdefOwnedDuring +
                    " cdefCertifiedDuring=" + cdefCertifiedDuring +
                    " cdefOwnedAfter=" + cdefOwnedAfter +
                    " cdefRestored=" + (cdefOwnedAfter == cdefOwnedBefore) +
                    " lightsaberRequirements=" +
                        joinRequirements(lightsaberRequirements) +
                    " lightsaberOwned=" +
                        hasCommand(player, LIGHTSABER_CERTIFICATION) +
                    " lightsaberCertified=" + lightsaberCertified;
            }
        }
        finally
        {
            for (int attempt = 0;
                temporaryCdefCommand &&
                    hasCommand(player, CDEF_CERTIFICATION) &&
                    attempt < 3;
                ++attempt)
            {
                revokeCommand(player, CDEF_CERTIFICATION);
            }
            if (temporaryCdefCommand &&
                hasCommand(player, CDEF_CERTIFICATION))
            {
                result = "error=cdefCommandRestoreFailed";
            }
            utils.removeScriptVar(player, CERTIFICATION_CACHE);
            if (hadCache)
            {
                utils.setScriptVar(player, CERTIFICATION_CACHE, originalCache);
            }
            if (isIdValid(cdef))
            {
                destroyObject(cdef);
            }
            if (isIdValid(lightsaber))
            {
                destroyObject(lightsaber);
            }
        }
        return result;
    }

    private String joinRequirements(String[] requirements)
    {
        if (requirements == null || requirements.length == 0)
        {
            return "none";
        }
        String result = "";
        for (int i = 0; i < requirements.length; ++i)
        {
            if (i > 0)
            {
                result += ",";
            }
            result += requirements[i];
        }
        return result;
    }
}
