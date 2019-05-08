package utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONObject;

/**
*
* @author Ivan Cornejo Quintana <cornejo.quintana@hotmail.com>
* @licence MIT
* @version 1.0
*/

public class ReadCfdi {
	
	private HashMap<String, HashMap>  cfdi;
    
    public ReadCfdi(){}
    
    /**
     * 
     * @param element
     * @param string
     * @return String value
     */
    private String evaluateString(Element element, String string[]){
        int size = string.length;
        String value =  null;
        for (int i = 0; i < size; i++) {
            if(element.getAttributeValue(string[i]) != null){
                value = element.getAttributeValue(string[i]);
                break;
            }
        }
        return value;
    }
    
    /**
     * 
     * @param dir
     * @return HashMap<Integer, HashMap>
     */
    public HashMap<String, HashMap> read(File dir){
        HashMap<String, HashMap>  CFDI = new HashMap();
        try {
            System.out.println("ok...");
            if(dir != (null)){
                System.out.println("Read file...");
                SAXBuilder builder =  new SAXBuilder();
                //Se crea el documento a traves del archivo
                Document document = (Document) builder.build(dir);
                //Se obtiene la raiz
                Element rootNote =  document.getRootElement();

                //Se obtiene la lista de hijos de la raiz
                List list = rootNote.getChildren();
                System.out.println("size: "+list.size());
                HashMap<String, String> root = new HashMap();
                root.put("Version", evaluateString(rootNote, new String [] {"Version", "version"}));
                root.put("Serie", evaluateString(rootNote,  new String [] {"Serie", "serie"}));
                root.put("Folio", evaluateString(rootNote, new String [] {"Folio", "folio"}));
                root.put("Fecha", evaluateString(rootNote, new String [] {"Fecha", "fecha"}));
                root.put("Certificado", evaluateString(rootNote, new String [] {"Certificado", "certificado"}));
                root.put("NoCertificado", evaluateString(rootNote, new String [] {"NoCertificado", "noCertificado", "Nocertificado", "nocertificado"}));
                root.put("FormaPago", evaluateString(rootNote, new String [] {"FormaPago", "formaPago", "Formapago", "formapago"}));
                root.put("SubTotal", evaluateString(rootNote,  new String [] {"SubTotal", "subTotal", "Subtotal", "subtotal"}));
                root.put("Moneda", evaluateString(rootNote, new String [] {"Moneda", "moneda"}));
                root.put("TipoCambio", evaluateString(rootNote, new String [] {"TipoCambio", "tipoCambio", "Tipocambio", "tipocambio"}));
                root.put("Total", evaluateString(rootNote, new String [] {"Total", "total"}));
                root.put("TipoDeComprobante", evaluateString(rootNote, new String [] {"TipoDeComprobante", "tipodeComprobante", "TipoDecomprobante" , "tipodecomprobante"}));
                root.put("MetodoPago", evaluateString(rootNote, new String [] {"MetodoPago", "metodoPago", "Metodopago", "metodopago"}));
                root.put("LugarExpedicion", evaluateString(rootNote,  new String [] {"LugarExpedicion", "lugarExpedicion", "Lugarexpedicion", "lugarexpedicion"}));
                
                ///Se recorre la lista de hijos
                for ( int i = 0; i < list.size(); i++ )
                {
                    Element element = (Element) list.get(i);
                    System.out.println(element.getName());
                    String nameElement = element.getName();
                    if(nameElement.equals("Emisor")){
                        HashMap<String, String>  ArrayEmisor = new HashMap();
                            ArrayEmisor.put("RegimenFiscal", evaluateString(element, new String [] {"RegimenFiscal", "regimenFiscal", "regimenfiscal"}));
                            ArrayEmisor.put("Nombre", evaluateString(element, new String [] {"Nombre", "nombre"}));
                            ArrayEmisor.put("Rfc", evaluateString(element, new String [] {"Rfc", "RFC", "rfc"}));
                        CFDI.put("Emisor", ArrayEmisor);
                    }
                    if(nameElement.equals("Receptor")){
                        HashMap<String, String>  Arrayreceptor = new HashMap();
                            Arrayreceptor.put("UsoCFDI", evaluateString(element, new String [] {"UsoCFDI", "usoCFDI", "usocfdi"}));
                            Arrayreceptor.put("Nombre", evaluateString(element, new String [] {"Nombre", "nombre"}));
                            Arrayreceptor.put("Rfc", evaluateString(element, new String [] {"Rfc", "RFC", "rfc"}));
                        CFDI.put("Receptor", Arrayreceptor);
                    }
                    if(nameElement.equals("Conceptos")){
                        List listConceptos  = element.getChildren();
                        HashMap<Integer, HashMap>  productos = new HashMap();
                        for (int j = 0; j < listConceptos.size(); j++) {
                            Element concepto = (Element) listConceptos.get(j);
                            HashMap<String, String> arreglo = new HashMap();
                            arreglo.put("Importe", evaluateString(concepto, new String [] {"Importe", "importe"}));
                            arreglo.put("ValorUnitario", evaluateString(concepto,  new String [] {"ValorUnitario", "valorUnitario", "valorunitario", "Valorunitario"}));
                            arreglo.put("Descripcion", evaluateString(concepto, new String [] {"Descripcion", "descripcion"}));
                            arreglo.put("Unidad", evaluateString(concepto, new String [] {"Unidad", "unidad"}));
                            arreglo.put("ClaveUnidad", evaluateString(concepto, new String [] {"ClaveUnidad", "claveUnidad", "claveunidad", "Claveunidad"}));
                            arreglo.put("Cantidad", evaluateString(concepto, new String [] {"Cantidad", "cantidad"}));
                            arreglo.put("ClaveProdServ", evaluateString(concepto, new String [] {"ClaveProdServ", "claveProdServ", "claveprodServ", "claveprodserv"}));

                            List listImpuestos  = concepto.getChildren();
                            for (int k = 0; k < listImpuestos.size(); k++) {
                                Element impuestos =  (Element) listImpuestos.get(k);
                                List listTraslados  = impuestos.getChildren();

                                for (int m = 0; m < listTraslados.size(); m++) {
                                    Element traslados = (Element) listTraslados.get(m);
                                    List listTraslado = traslados.getChildren();
                                    for (int n = 0; n < listTraslado.size(); n++) {
                                        Element Traslado = (Element) listTraslado.get(m);
                                        arreglo.put("Importe", evaluateString(Traslado, new String [] {"Importe", "importe"}));
                                        arreglo.put("TasaOCuota", evaluateString(Traslado, new String [] {"TasaOCuota", "tasaOCuota", "tasaoCuota", "tasaocuota", "Tasaocuota"}));
                                        arreglo.put("TipoFactor", evaluateString(Traslado, new String [] {"TipoFactor", "tipoFactor", "tipofactor", "Tipofactor"}));
                                        arreglo.put("Impuesto", evaluateString(Traslado, new String [] {"Impuesto", "impuesto"}));
                                        arreglo.put("Base", evaluateString(Traslado, new String [] {"Base", "base"}));
                                    }
                                }
                            }
                            productos.put(j, arreglo);
                        }
                        CFDI.put("Productos", productos);
                    }
                    if(nameElement.equals("Impuestos")){
                        HashMap<String, HashMap>  ImpuestosTraslado = new HashMap();
                        root.put("TotalImpuestosTrasladados", evaluateString(element, new String [] {"TotalImpuestosTrasladados", "totalImpuestosTrasladados", "TotalimpuestosTrasladados", "TotalImpuestostrasladados", "totalimpuestostrasladados"}));
                        List listImpuestos  = element.getChildren();
                        HashMap<Integer, HashMap>  ImpuestosTraslados = new HashMap();
                        
                        for (int k = 0; k < listImpuestos.size(); k++) {
                            Element impuestos =  (Element) listImpuestos.get(k);
                            List listTraslados  = impuestos.getChildren();
                            for (int m = 0; m < listTraslados.size(); m++) {
                                HashMap<String, String>  Traslados = new HashMap();
                                Element traslados = (Element) listTraslados.get(m);
                                Traslados.put("Importe", evaluateString(traslados, new String [] {"Importe", "importe"}));
                                Traslados.put("TasaOCuota", evaluateString(traslados, new String [] {"TasaOCuota", "tasaOCuota", "tasaoCuota", "tasaocuota", "Tasaocuota"}));
                                Traslados.put("TipoFactor", evaluateString(traslados, new String [] {"TipoFactor", "tipoFactor", "tipofactor", "Tipofactor"}));
                                Traslados.put("Impuesto", evaluateString(traslados, new String [] {"Impuesto", "impuesto"}));
                                ImpuestosTraslados.put(m, Traslados);
                            }
                        }
                        ImpuestosTraslado.put("Traslados", ImpuestosTraslados);
                        CFDI.put("Impuestos", ImpuestosTraslado);
                    }
                    if(nameElement.equals("Complemento")){
                        HashMap<String, String>  ArrayComplemento = new HashMap();
                        List listComplemento  = element.getChildren();
                        for (int k = 0; k < listComplemento.size(); k++) {
                            Element TimbreFiscalDigital = (Element) listComplemento.get(k);
                                ArrayComplemento.put("Version", evaluateString(TimbreFiscalDigital, new String [] {"Version", "version"}));
                                ArrayComplemento.put("UUID", evaluateString(TimbreFiscalDigital, new String [] {"UUID", "uuid"}));
                                ArrayComplemento.put("FechaTimbrado", evaluateString(TimbreFiscalDigital, new String [] {"FechaTimbrado", "fechaTimbrado", "Fechatimbrado", "fechatimbrado"}));
                                ArrayComplemento.put("RfcProvCertif", evaluateString(TimbreFiscalDigital, new String [] {"RfcProvCertif", "rfcProvCertif", "rfcprovCertif", "rfcprovcertif"}));
                                ArrayComplemento.put("SelloCFD", evaluateString(TimbreFiscalDigital, new String [] {"SelloCFD", "selloCFD", "sellocfd"}));
                                ArrayComplemento.put("NoCertificadoSAT", evaluateString(TimbreFiscalDigital, new String [] {"NoCertificadoSAT", "noCertificadoSAT", "nocertificadoSAT", "nocertificadosat"}));
                                ArrayComplemento.put("SelloSAT", evaluateString(TimbreFiscalDigital, new String [] {"SelloSAT", "selloSAT", "sellosat"}));
                        }
                        CFDI.put("Complemento", ArrayComplemento);
                    }
                }
                CFDI.put("CFDI", root);
                this.setCfdi(CFDI);
            }
            System.out.println("finish...");
        } catch (JDOMException | IOException ex) {
        	ex.printStackTrace();
        }
        return CFDI;
    }
    
    /**
     * 
     * @param dir
     * @return JSONObject
     */
    public JSONObject readJson(File dir){
        return new JSONObject(read(dir));
    }
    
    
    /**
     * 
     * @param map 
     * returns the HashMap reading
     */
    public void print(Map<String, HashMap> map)  
    {
        if (map.isEmpty())  
        { 
            System.out.println("map is empty"); 
        }
        else
        { 
            System.out.println(map); 
        } 
    }
    
    /**
     * returns the HashMap reading
     */
    public void print()  
    {
        if (this.cfdi.isEmpty())  
        { 
            System.out.println("map is empty"); 
        }
        else
        { 
            System.out.println(this.cfdi); 
        } 
    }

	public HashMap<String, HashMap> getCfdi() {
		return this.cfdi;
	}

	public void setCfdi(HashMap<String, HashMap> cfdi) {
		this.cfdi = cfdi;
	}

}
