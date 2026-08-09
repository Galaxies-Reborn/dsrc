package script.item.conversion;

import script.obj_id;

public class medicine extends script.base_script
{
    public medicine()
    {
    }
    public static void retirePostP14MedicineConversionScript(obj_id item) throws InterruptedException
    {
        if (isIdValid(item) && exists(item) && hasScript(item, "item.conversion.medicine"))
        {
            detachScript(item, "item.conversion.medicine");
        }
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        retirePostP14MedicineConversionScript(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        retirePostP14MedicineConversionScript(self);
        return SCRIPT_CONTINUE;
    }
}
