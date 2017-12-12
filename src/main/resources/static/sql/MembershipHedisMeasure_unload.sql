DELETE mhm FROM membership_hedis_measure  mhm  
  JOIN hedis_measure_rule  hmr  ON  hmr.hedis_msr_rule_Id  = mhm.hedis_msr_rule_id 
  WHERE hmr.ins_id=:insId