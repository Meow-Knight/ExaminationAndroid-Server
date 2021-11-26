package tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import common.SocketRequestType;
import dao.AccountDao;
import dao.ExaminationDao;
import dao.HistoryDao;
import dao.QuetionDao;
import entity.Account;
import entity.Examination;
import entity.History;
import entity.Question;
import util.SocketUtils;

public class SocketHandler extends Thread {
	
	private Socket clientSocket;
	
	public SocketHandler(Socket socket) {
		System.out.println("Client socket connected!");
		this.clientSocket = socket;
	}
	
	@Override
	public void run() {
        ObjectInputStream din = null;
        ObjectOutputStream dos = null;

        try {
            dos = new ObjectOutputStream(clientSocket.getOutputStream());
            din = new ObjectInputStream(clientSocket.getInputStream());
            
            while (true) {
            	System.out.println("Ready getting request ...");
	            String clientRequest = din.readUTF();
	            System.out.println("Client request: " + clientRequest);
	            SocketRequestType requestType = SocketUtils.getSocketRequestType(clientRequest);
	            
	            switch(requestType) {
	            case GET_EXAMINATIONS:
	            	List<Examination> examinations = ExaminationDao.getInstance().getAllExamination();
	            	examinations.forEach(System.out::println);
	            	dos.writeObject(examinations);
	            	break;
	            case GET_LIST_QUESTIONS_BY_EXAM_ID:
	                Long examId = din.readLong();
	                System.out.println("examId: " + examId);
	            	List<Question> questions = QuetionDao.getInstance().getQuestionsByExamId(examId);
	            	dos.writeObject(questions);
	            	break;
	            case ASYNC_ACCOUNT:
	            	Account asyncAccount = (Account) din.readObject();
	            	AccountDao dao = AccountDao.getInstance();
	            	if (!dao.isAccountExisted(asyncAccount.getEmail())) {
	            		dao.createAccount(asyncAccount);
	            	}
	            	break;
	            case SAVE_HISTORY_RECORD:
	            	History orderHistory = (History) din.readObject();
	            	QuetionDao.getInstance().saveNewRecord(orderHistory);
	            	break;
	            case GET_HISTORY_OF_ACCOUNT:
	                String accountEmail = din.readUTF();
	                List<History> histories = HistoryDao.getInstance().getHistoryByAccount(accountEmail);
	            	dos.writeObject(histories);
	            	break;
				default:
					System.out.println("Not found this request");
					break;
	            }
	            dos.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
        	System.out.println("==== Client socket is closed ===");
        }
        try {
        	dos.close();
            din.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
