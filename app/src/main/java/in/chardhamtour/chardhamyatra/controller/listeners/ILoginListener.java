package in.chardhamtour.chardhamyatra.controller.listeners;


public interface ILoginListener {
    void onLoginSuccess();
    void onLoginFailed(String client);
    void onLoginStart(String name,String email, String password, final String client, final String clientId,final String image_url);
}
