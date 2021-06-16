package in.chardhamtour.chardhamyatra.controller.listeners;

public interface IResponseListener {
   void onResponseSuccess(int dataSize);
   void onResponseFailed();
}
