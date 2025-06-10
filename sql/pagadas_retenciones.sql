SELECT p.anho presupuesto, 
       o.orga_id||' '||o.denominacion organismo, 
	   pi.unad_id cod_unidad_administradora, 
	   ua.denominacion desc_unidad_administradora, 
       p.pago_id orden, 
       DECODE(pi.autorizado_id, NULL, pi.nombre_benef||' '||pi.apellido_benef, pi.nombre_autori||' '||pi.apellido_autori) beneficiario, 
       r.RETE_ID||'-'||rt.DENOMINACION, 
       DECODE(pi.autorizado_id, NULL, pi.ident_benef, pi.ident_autori) rif, 
       pi.monto_bruto monto_orden_ant, 
       DECODE(p.anho , 2007, ROUND((pi.monto_bruto/1000) ,2), 2006, ROUND((pi.monto_bruto/1000) ,2), pi.monto_bruto) monto_orden, 
       r.monto monto_1_x_500_ant, 
       DECODE(p.anho , 2007, ROUND((r.monto/1000) ,2), 2006, ROUND((r.monto/1000) ,2), r.monto) monto_1_x_500, 
       p.fecha_pago 
FROM pago p, pago_imputado pi, retencion_x_pago r, organismo o,retencion rt, unidad_administradora ua 
WHERE p.anho = pi.anho 
  AND p.orga_id = pi.orga_id 
  AND p.pago_id = pi.pago_id 
  AND p.tipg_id = pi.tipg_id 
  AND p.anho = r.anho 
  AND p.orga_id = r.orga_id 
  AND p.pago_id = r.pago_id 
  AND p.tipg_id = r.tipg_id 
  AND p.orga_id = o.orga_id 
  AND R.RETE_ID = Rt.RETE_ID 
  AND R.ORGA_ID = Rt.ORGA_ID 
  AND R.TIRE_ID = Rt.TIRE_ID 
  AND ua.unad_id = pi.unad_id 
  AND ua.anho = pi.anho 
  AND ua.orga_id = pi.orga_id 
  AND p.tipg_id IN (1,10) 
  AND p.razon_id_fin IN (4,10,11,12,13) 
  AND TO_DATE(fecha_pago,'DD/MM/RRRR') BETWEEN TO_DATE(:PAR_DESDE,'DD/MM/RRRR') AND TO_DATE(:PAR_HASTA,'DD/MM/RRRR') 
ORDER BY 1,2,3 ASC