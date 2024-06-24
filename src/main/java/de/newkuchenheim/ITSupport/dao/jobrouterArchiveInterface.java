package de.newkuchenheim.ITSupport.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 14.02.2024
 * 
 */
public interface jobrouterArchiveInterface {
	//################################################### region JobArchvie archives modul ###################################################
	/**
	 * send a request to lists all archived documents for the given archive that match filters.
	 * Meta fields, index fields and containing files are displayed for each document.
	 * Assigned keywords are not listed.
	 * 
	 * @param archive name or guid of archive
	 * 
	 * @return JSONArray object with archivedocument data if request was sent success. Otherwise null when failure.
	 */
	public JSONArray getArchive(String archive);
	/**
	 * send a request to store fulltext index for all files of a document revision in the given archive
	 * 
	 * @param archive name or guid of archive
	 * 
	 * @return boolean true if request was sent success. Otherwise false when failure.
	 */
	public boolean sendArchiveFulltextIndex(String archive);
	//################################################ endregion JobArchvie archives modul ###################################################
	//############################################### region JobArchvie archivedocuments modul ###############################################
	/**
	 * send a request to lists all archived documents for the given archive that match filters.
	 * Meta fields, index fields, assigned keywords and containing files are displayed for each document.
	 * 
	 * @param archive name or guid of archive
	 * 
	 * @return JSONArray object with archivedocument data if request was sent success. Otherwise null when failure.
	 */
	public JSONArray getArchiveDocuments(String archive);
	//############################################# endregion JobArchvie archivedocuments modul ##############################################
	//############################################# region JobArchvie archivedocumentsrevisions modul ########################################
	//region JobArchive archivedocumentrevisions modul
	/**
	 * send a request to get the document file of the archived document with the given revision id.
	 * 
	 * @param archive name or guid of archive
	 * @param revisionid Revision ID
	 * 
	 * @return File data as HashMap if request was sent success. Otherwise null when failure.
	 */
	public Map<String, Object> getArchiveDocumentRevisionsFile(String archive, long revisionid);
	/**
	 * send a request to get all files of the archived document with the given revision id.
	 * 
	 * @param archive name or guid of archive
	 * @param revisionid Revision ID
	 * 
	 * @return Zip-File data as HashMap if request was sent success. Otherwise null when failure.
	 */
	public Map<String, Object> getArchiveDocumentRevisionsFiles(String archive, long revisionid);
	/**
	 * send a request to get a single clipped file of the archived document with the given revision id and clipped file id.
	 * 
	 * @param archive name or guid of archive
	 * @param revisionid Revision ID
	 * @param clippedfileid Clipped File ID
	 * 
	 * @return File data as HashMap if request was sent success. Otherwise null when failure.
	 */
	public Map<String, Object> getArchiveDocumentRevisionsClippedFile(String archive, long revisionid, long clippedfileid);
	/**
	 * send a request to get all clipped files of the archived document with the given revision id.
	 * 
	 * @param archive name or guid of archive
	 * @param revisionid Revision ID
	 * 
	 * @return Zip-File data as HashMap if request was sent success. otherwise null
	 */
	public Map<String, Object> getArchiveDocumentRevisionsClippedFiles(String archive, long revisionid);
	/**
	 * send a request to get a single PDF file with the merged content of all document files.
	 * 
	 * @param archive name or guid of archive
	 * @param revisionid Revision ID
	 * 
	 * @return PDF File data as HashMap if request was sent success. Otherwise null when failure.
	 */
	public Map<String, Object> getArchiveDocumentRevisionsPDF(String archive, long revisionid);
	/**
	 * send a request to create a completely new document revision in the given archive.
	 * The first file from the request becomes the document file.
	 * Any additional files from the request are added as clipped files
	 * 
	 * @param archive name or guid of archive
	 * @param List of uploaded files
	 * 
	 * @return long revisionid if request was sent success. Otherwise -1 when failure.
	 */
	public long sendArchiveDocumentRevisionsFile(String archive, List<MultipartFile> uploadedFiles);
	/**
	 * send a request to Clips files to the archived document with the given revision id.
	 * A new revision will be created and the associated revision id returned. 
	 * 
	 * @param archive name or guid of archive
	 * @param list of uploaded files
	 * 
	 * @return long revisionid if request was sent success. Otherwise -1 when failure.
	 */
	public long sendArchiveDocumentRevisionsClippedFiles(String archive, List<MultipartFile> uploadedFiles);
	/**
	 * send a request to create a new revision of an already existing document in the given archive.
	 * The first file from the request becomes the new document file.
	 * Already present clipped files from the base revision are copied to the new document revision.
	 * Any additional files from the request are added as clipped files. 
	 * 
	 * @param archive name or guid of archive
	 * @param revisionid Revision ID of an existing document
	 * @param list of uploaded files
	 * 
	 * @return long revisionid if request was sent success. Otherwise -1 when failure.
	 */
	public long sendArchiveDocumentRevisionsFileRevID(String archive, long revisionid, List<MultipartFile> uploadedFiles);
	/**
	 * send a request to delete a document with the given revision id.
	 * 
	 * @param archive name or guid of archive
	 * @param revisionid Document revision identifier
	 * 
	 * @return boolean true if request was sent success. Otherwise false when failure.
	 */
	public boolean deleteArchiveDocumentRevision(String archive, long revisionid);
	//########################################### endregion JobArchvie archivedocumentsrevisions modul #######################################
	//############################################ region JobArchvie archiveindicies modul ###################################################
	/**
	 * send a request to get data for given index field that match filters. Labels are returned in the user language.
	 * 
	 * @param archive name or guid of archive
	 * @param indexfield name of index field
	 * 
	 * @return JSONArray with indicies if request was sent success. Otherwise null.
	 */
	public JSONArray getArchiveIndicies(String archive, String indexfield);
	/**
	 * send a request to get the available list options for given index field. Translatable labels are returned in the user language.
	 * 
	 * @param archive name or guid of archive
	 * @param indexfield name of index field
	 * 
	 * @return JSONArray with indicies if request was sent success. Otherwise null.
	 */
	public JSONArray getArchiveIndiciesListOptions(String archive, String indexfield);
	/**
	 * send a request to get an object containing the list options for each list field with configured SQL statement.
	 * Current values of other list fields can be provided for variable resolution in the SQL statements.
	 * If a document revision id is provided (field revisionId in the request body), index field values which are missing in the request will be read from the archive table in the database.
	 * The authorized user needs an active archive profile to access this route. 
	 * 
	 * @param archive name or guid of archive
	 * @param fields as JSONObject
	 * 
	 * @return JSONArray with listoptions if request was sent success. Otherwise null.
	 */
	public JSONArray postArchiveIndicies(String archive, JSONObject fields);
	/**
	 * send a request to get an object containing the list options for the given list field.
	 * The field should be configured with SQL statement. Current values of other list fields can be provided for variable resolution in the SQL statement.
	 * If a document revision id is provided (field revisionId in the request body), index field values which are missing in the request will be read from the archive table in the database.
	 * The authorized user needs an active archive profile to access this route. 
	 * 
	 * @param archive name or guid of archive
	 * @param indexfield name of index field
	 * @param fields as JSONObject
	 * 
	 * @return JSONArray with listoptions if request was sent success. Otherwise null.
	 */
	public JSONArray postArchiveIndiciesIndexField(String archive, String indexfield, JSONObject fields);
	//########################################## endregion JobArchvie archiveindicies modul ##################################################
	//######################################### region JobArchvie archiveindexdialogs modul ##################################################
	/**
	 * send a request to get all index dialogs for the given archive 
	 * 
	 * @param archive name or guid of archive
	 * 
	 * @return JSONArray with all data for all dialogs if request was sent success. Otherwise null.
	 */
	public JSONArray getArchiveIndexDialogs(String archive);
	/**
	 * send a request to get all index dialogs for the given archive 
	 * 
	 * @param archive name or guid of archive
	 * @param IndexDialogName Name or GUID of the index dialog
	 * 
	 * @return JSONArray with all data of given dialog if request was sent success. Otherwise null.
	 */
	public JSONArray getArchiveIndexDialogsName(String archive, String IndexDialogName);
	//######################################## endregion JobArchvie archiveindexdialogs modul ################################################
	//############################################# region JobArchvie archiveviews modul #####################################################
	/**
	 * send a request to get a PDF file containing all PDF files of the requested documents.
	 * The revision Ids of the documents that should be downloaded are transferred in the request body. 
	 * 
	 * @param archive name or guid of archive
	 * @param viewId Id of the archive view
	 * 
	 * @return PDF File data as HashMap if request was sent success. Otherwise null.
	 */
	public Map<String, Object> getArchiveViews(String archive, long viewId);
	//########################################### endregion JobArchvie archiveviews modul ####################################################
	//########################################## region JobArchvie archiveviewindcies modul ##################################################
	/**
	 * send a request to get an object containing the list options for each list field.
	 * Current list field values can be provided for variable resolution in SQL statements for list fields with SQL options.
	 * If a document revision id is provided (field revisionId in the request body), index field values which are missing in the request will be read from the archive table in the database.
	 * The authorized user needs an active archive profile to access this route. 
	 * 
	 * @param archive name or guid of archive
	 * @param viewId Id of the archive view
	 * 
	 * @return JSONArray with listoptions if request was sent success. Otherwise null.
	 */
	public JSONArray getArchiveViewIndicies(String archive, long viewId);
	//############################################ endregion JobArchvie archiveviewindcies modul #############################################
}
