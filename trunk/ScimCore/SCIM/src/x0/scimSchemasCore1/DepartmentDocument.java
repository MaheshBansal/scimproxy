/*
 * An XML document type.
 * Localname: department
 * Namespace: urn:scim:schemas:core:1.0
 * Java type: x0.scimSchemasCore1.DepartmentDocument
 *
 * Automatically generated - do not modify.
 */
package x0.scimSchemasCore1;


/**
 * A document containing one department(@urn:scim:schemas:core:1.0) element.
 *
 * This is a complex type.
 */
public interface DepartmentDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(DepartmentDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s49AFFD295516B9521E99EB5B1895FF95").resolveHandle("departmentb168doctype");
    
    /**
     * Gets the "department" element
     */
    java.lang.String getDepartment();
    
    /**
     * Gets (as xml) the "department" element
     */
    org.apache.xmlbeans.XmlString xgetDepartment();
    
    /**
     * Sets the "department" element
     */
    void setDepartment(java.lang.String department);
    
    /**
     * Sets (as xml) the "department" element
     */
    void xsetDepartment(org.apache.xmlbeans.XmlString department);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static x0.scimSchemasCore1.DepartmentDocument newInstance() {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static x0.scimSchemasCore1.DepartmentDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static x0.scimSchemasCore1.DepartmentDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static x0.scimSchemasCore1.DepartmentDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (x0.scimSchemasCore1.DepartmentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
