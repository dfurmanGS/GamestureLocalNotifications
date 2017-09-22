#if UNITY_ANDROID
using UnityEngine;

public class DisplayText : MonoBehaviour
{

	private AndroidJavaClass _localNotificationManager;
	// Use this for initialization
	void Start () 
	{	
		_localNotificationManager = new AndroidJavaClass("com.gamesture.gamesturelocalnotifications.NotificationsManager");
	}

	public void OnClickSendNotification()
	{
		_localNotificationManager.CallStatic("SendNotification", Random.Range(1,9999), "Super Tytuł", "Działa dupa1234 super Questland! yeah!", (long)10);
	}
	
	public void OnClickSendNotificationInternal()
	{
		_localNotificationManager.CallStatic("SendNotificationInternal", Random.Range(1,9999), "Super Tytuł", "Działa dupa1234 super Questland! yeah!", (long)0);
	}
	
}
#endif
