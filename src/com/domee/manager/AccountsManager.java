package com.domee.manager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.domee.model.Account;
import com.domee.model.User;
import com.weibo.sdk.android.Oauth2AccessToken;

public class AccountsManager {

	private static final String ACCOUNTS_FILE_PATH = "account.txt";
	private static Context context = null;
	public  static List<Account> accounts = null;
	public  static Account curAccount = null;

	/**
	 * 初始化Context，给其它方法使用
	 * @param theContext
	 */
	public static void initContext(Context theContext) {
        context = theContext;
        accounts = getAccounts();
		if (accounts != null && accounts.size() != 0) {
			curAccount = accounts.get(0);
		} else {
			accounts = new ArrayList<Account>();
		}
	}

	public static List<Account> getAccountList() {
		return  accounts;
	}
	
	public static Account getCurAccount() {
		if (accounts != null && accounts.size() > 0)
			return accounts.get(0);
		else {
			return null;
		}
	}
	
	public static User getCurUser(){
	    return getCurAccount().getUser();	
	}

	public static void createAccount(Account account) {
		int index = 0;
		for (Account acc : accounts) {
			if (acc.getLoginId().equals(account.getLoginId())) {
				break;
			}
			index++;
		}
		if (index == accounts.size()) {
			accounts.add(0, account);
		} else {
			accounts.remove(index);
			accounts.add(0, account);
		}
		saveAccountList();
	}
	
	public void delAccount(int index) {
		accounts.remove(index);
		saveAccountList();
	}

	public void updateAccount(Account account) {
		int index = 0;
		for (Account acc : accounts) {
			if (acc.getLoginId().equals(account.getLoginId())) {
				break;
			}
			index++;
		}
		accounts.remove(index);
		accounts.add(index, account);
		saveAccountList();
	}

	public static Account getAccount(int index) {
		return accounts.get(index);
	}

	public static void saveAccountList() {
		try {

			FileOutputStream fos = context.openFileOutput(ACCOUNTS_FILE_PATH,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(accounts);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<Account> getAccounts() {
		List<Account> accountList = null;
		try {

			FileInputStream fis = context.openFileInput(ACCOUNTS_FILE_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			accountList = (List<Account>) ois.readObject();
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accountList;
	}

	public static Oauth2AccessToken curAccessToken() {

		Account curAccount = AccountsManager.curAccount;
		Oauth2AccessToken accessToken = new Oauth2AccessToken(curAccount.getmAccessToken(), curAccount.getmExpiresTime());
		return accessToken;
	}
}
