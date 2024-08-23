package de.newkuchenheim.ITSupport.dao.implement;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.awNote;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskKanboardConfiguration;
import de.newkuchenheim.ITSupport.dao.kanboardDAO;
import de.newkuchenheim.ITSupport.dao.kanboardTaskInterface;

public class awsnotizKanboardDAO extends kanboardDAO implements kanboardTaskInterface<awNote>{

	private static awsnotizKanboardDAO instance;

	/**
	 * Get Instance kanboardDAO
	 * 
	 * @return kanboardDAO
	 */
	public static awsnotizKanboardDAO getInstance() {
		if (instance == null) {
			return new awsnotizKanboardDAO();
		}
		return instance;
	}

	@Override
	public int sendTicket(awNote ticket) throws UnsupportedEncodingException {
		if (ticket != null) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String fromDate = ticket.getAwvonDate().format(formatter), toDate = "";

			/////////////////////////////////////////////////////////////////////////////////
			// wenn bisDate null ist -> unbestimmte Enddatum = "auf weiteres"
			if (ticket.getAwtoDate() != null) {
				toDate = ticket.getAwtoDate().format(formatter);
			} else {
				toDate = "auf Weiteres";
			}
			////////////////////////////////////////////////////////////////////////////////

			// fester Mustertext
			String title = "[" + ticket.getMname() + "] [" + fromDate + " - " + toDate + "]";

			String body = "Sehr geehrte Damen und Herren, " + _NEWLINE + _NEWLINE + "ich bin vom " + fromDate + " bis "
					+ toDate + " nicht im Hause." + _NEWLINE
					+ "Bitte beachten Sie, dass Ihre Mail nicht automatisch weitergeleitet wird." + _NEWLINE;

			// Problem mit dem Binding von Object mit form über Typ booleen -> check, ob
			// Kontakt 1 und 2 leer sind?
			boolean isVertreter1 = ticket.getKontakt1() != null && !ticket.getKontakt1().isBlank();
			boolean isVertreter2 = ticket.getKontakt2() != null && !ticket.getKontakt2().isBlank();
			boolean isVertreter = isVertreter1 || isVertreter2;

			// falls kein vertreter
			if (!isVertreter) {
				// set German Date format

				body += _NEWLINE + "Mit freundlichen Grüßen," + _NEWLINE + ticket.getMname();

				// if fromDate im Intevall +1 oder -1 liegt, veschiebe ticket direkt in
				// Bereit-Spalte (77)
				// ansonsten in Eingang (76)
				LocalDate _heute = LocalDate.now();
				TaskKanboardConfiguration task = TaskKanboardConfiguration.CREATE_TASK;
				
				if (_heute.isEqual(ticket.getAwvonDate()) || _heute.isEqual(ticket.getAwvonDate().minusDays(1))
						|| _heute.isEqual(ticket.getAwvonDate().plusDays(1))) {
					if (ticket.getAwtoDate() != null) {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 77);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_due", ticket.getAwtoDate().toString());
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
						
					} else {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 77);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
					}
				} else {
					if (ticket.getAwtoDate() != null) {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 76);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_due", ticket.getAwtoDate().toString());
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
						
					} else {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 76);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
					}
				}
				Object result = sendTaskRequest(task);
				if(result instanceof Integer) {
					return (int) result;
				} 
				else 
					return -1;
					
			} else { // sonst mit Vertreter 1 oder 2 oder beiden
				String vertreter = "";

				if (ticket.getKontakt1() != null && !ticket.getKontakt1().isBlank()) {
					vertreter = _TAB_UL_1 + ticket.getKontakt1(); // neue Zeile mit list *

					String _tel = ticket.getTelnr1(), _mail = ticket.getMail1();
					if (_tel != null && !_tel.isBlank()) { // überprüfen zuerst Kontak 1
						vertreter += " telefonisch unter " + _tel;
					}
					if (_mail != null && !_mail.isBlank()) {
						if (_tel != null && !_tel.isBlank() && _mail != null && !_mail.isBlank()) {
							vertreter += " oder unter " + _mail;
						} else
							vertreter += " unter " + _mail;
					}

					if (ticket.getKontakt2() != null && !ticket.getKontakt2().isBlank()) { // dann überprüfen Kontakt 2,
																							// falls nicht leer, dann
																							// fügen daten hinzu
						vertreter += _NEWLINE + _TAB_UL_1 + ticket.getKontakt2();
						String _mail2 = ticket.getMail2(), _tel2 = ticket.getTelnr2();
						if (_tel2 != null && !_tel2.isBlank()) {
							vertreter += " telefonisch unter " + _tel2;
						}
						if (_mail2 != null && !_mail2.isBlank()) {
							_mail2 = ticket.getMail2();
							if (_tel2 != null && !_tel2.isBlank() && _mail2 != null && !_mail2.isBlank()) {
								vertreter += " oder unter " + _mail2;
							} else
								vertreter += " unter " + _mail2;
						}
					}
				} else {
					vertreter = _TAB_UL_1 + ticket.getKontakt2(); // neue Zeile nut für kontakt 2 mit List *, wenn
																	// kontakt 1 nicht ausgefüllt wird

					String _mail2 = ticket.getMail2(), _tel2 = ticket.getTelnr2();
					if (_tel2 != null && !_tel2.isBlank()) {
						vertreter += " telefonisch unter " + _tel2;
					}
					if (_mail2 != null && !_mail2.isBlank()) {
						_mail2 = ticket.getMail2();
						if (_tel2 != null && !_tel2.isBlank() && _mail2 != null && !_mail2.isBlank()) {
							vertreter += " oder unter " + _mail2;
						} else
							vertreter += " unter " + _mail2;
					}
				}

				// fügen Kontakte in Message Body
				body += "In dringenden Fällen wenden Sie sich daher bitte an " + _NEWLINE + vertreter + _NEWLINE
						+ _NEWLINE + "Mit freundlichen Grüßen," + _NEWLINE + ticket.getMname();
				/////////////////////////////////////////////////////////////////////////////////////

				// if fromDate im Intevall +1 oder -1 liegt, veschiebe ticket direkt in
				// Bereit-Spalte (77)
				// ansonsten in Eingang (76)
				LocalDate _heute = LocalDate.now();
				TaskKanboardConfiguration task = TaskKanboardConfiguration.CREATE_TASK;
				
				String new_desc = "";
				if (body != null && !body.isBlank() && body.contains(System.lineSeparator())) {
					//String[] _str_split = description.split(System.lineSeparator());
					String[] _str_split = body.split("\\R");
					for (String s : _str_split) {
						new_desc = new_desc + s + _NEWLINE;
					}
					body = new_desc;
				}
				
				if (_heute.isEqual(ticket.getAwvonDate()) || _heute.isEqual(ticket.getAwvonDate().minusDays(1))
						|| _heute.isEqual(ticket.getAwvonDate().plusDays(1))) {
					if (ticket.getAwtoDate() != null) {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 77);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_due", ticket.getAwtoDate().toString());
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
						
					} else {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 77);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
					}
					
				} else {
					if (ticket.getAwtoDate() != null) {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 76);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_due", ticket.getAwtoDate().toString());
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
						
					} else {
						task.setParameterValue("project_id", 18);
						task.setParameterValue("creator_id", 18);
						task.setParameterValue("swimmland_id", 61);
						task.setParameterValue("color_id", "green");
						task.setParameterValue("column_id", 76);
						task.setParameterValue("title", title);
						task.setParameterValue("description", body);
						task.setParameterValue("date_started", ticket.getAwvonDate().toString());
					}
					
				}
				Object result = sendTaskRequest(task);
				if(result instanceof Integer) {
					return (int) result;
				} 
				else 
					return -1;
				
			}
		}
		return -1;
	}

	@Override
	public awNote getTicketByName(awNote ticket) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}
}
