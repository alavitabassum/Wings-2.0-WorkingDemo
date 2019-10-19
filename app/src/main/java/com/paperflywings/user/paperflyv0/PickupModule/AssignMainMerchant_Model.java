package com.paperflywings.user.paperflyv0.PickupModule;

public class AssignMainMerchant_Model {
    private String main_merchant;
    private String merchant_code;

    public AssignMainMerchant_Model(String main_merchant, String merchant_code) {
        this.main_merchant = main_merchant;
        this.merchant_code = merchant_code;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getMain_merchant() {
        return main_merchant;
    }

    public void setMain_merchant(String main_merchant) {
        this.main_merchant = main_merchant;
    }
}
