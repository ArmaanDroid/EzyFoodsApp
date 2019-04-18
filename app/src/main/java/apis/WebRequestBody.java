package apis;

import com.google.gson.annotations.SerializedName;

public final class WebRequestBody {
    @SerializedName("email")
    private String email;
    @SerializedName("facebookId")
    private String facebookId;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("password")
    private String password;
    @SerializedName("firebaseToken")
    private String firebaseToken;
    @SerializedName("name")
    private String name;
    @SerializedName("dob")
    private String dob;
    @SerializedName("userId")
    private String userId;
    @SerializedName("blockId")
    private String blockId;
    @SerializedName("reportId")
    private String reportId;
    @SerializedName("favId")
    private String favId;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("online_status")
    private String online_status;
    @SerializedName("height")
    private String height;
    @SerializedName("ethnicity")
    private String ethnicity;
    @SerializedName("position")
    private String position;
    @SerializedName("tribes")
    private String tribes;
    @SerializedName("lookingFor")
    private String lookingFor;
    @SerializedName("hiv_status")
    private String hiv_status;
    @SerializedName("last_test")
    private String last_test;
    @SerializedName("facebook_id")
    private String facebook_id;
    @SerializedName("minHeight")
    private String minHeight;
    @SerializedName("minWeight")
    private String minWeight;
    @SerializedName("minAge")
    private String minAge;
    @SerializedName("maxHeight")
    private String maxHeight;
    @SerializedName("maxWeight")
    private String maxWeight;
    @SerializedName("maxAge")
    private String maxAge;
    @SerializedName("relationshipStatus")
    private String relationshipStatus;
    @SerializedName("onlineNow")
    private String onlineNow;
    @SerializedName("bodyType")
    private String bodyType;
    @SerializedName("showMe")
    private String showMe;
    @SerializedName("photosOnly")
    private String photosOnly;
    @SerializedName("aboutMe")
    private String aboutMe;
    @SerializedName("requestName")
    private String requestName;
    @SerializedName("showDistance")
    private String showDistance;
    @SerializedName("newPassword")
    private String newPassword;
    @SerializedName("hivStatus")
    private String hivStatus;
    @SerializedName("lastTest")
    private String lastTest;
    @SerializedName("visitId")
    private String visitId;
    @SerializedName("lastCount")
    private String lastCount;
    @SerializedName("opponentId")
    private String opponentId;
    @SerializedName("gender")
    private String gender;
    @SerializedName("stripeToken")
    private String stripeToken;
    @SerializedName("reason")
    private String reason;
    @SerializedName("planId")
    private String planId;
    @SerializedName("accountId")
    private String accountId;
    @SerializedName("bankHolderName")
    private String bankHolderName;
    @SerializedName("bankAccountNo")
    private String bankAccountNo;
    @SerializedName("sellerAccount")
    private String sellerAccount;
    @SerializedName("address")
    private String address;
    @SerializedName("zipcode")
    private String zipcode;
    @SerializedName("phone")
    private String phone;
    @SerializedName("stripeAccountId")
    private String stripeAccountId;


    @SerializedName("title")
    private String title;
    @SerializedName("preparedBy")
    private String preparedBy;
    @SerializedName("orderBy")
    private String orderBy;
    @SerializedName("price")
    private String price;
    @SerializedName("availableOn")
    private String availableOn;
    @SerializedName("spiceLevel")
    private String spiceLevel;
    @SerializedName("cuisineType")
    private String cuisineType;
    @SerializedName("pickUp")
    private String pickUp;
    @SerializedName("delivery")
    private String delivery;
    @SerializedName("weight")
    private String weight;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("description")
    private String description;
    @SerializedName("dishes")
    private String[] dishes;
    @SerializedName("count")
    private String[] count;
    @SerializedName("dishId")
    private String dishId;
    @SerializedName("comment")
    private String comment;
    @SerializedName("rating")
    private String rating;
    @SerializedName("refered_by")
    private String refered_by;
    @SerializedName("userType")
    private String userType;

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setRefered_by(String refered_by) {
        this.refered_by = refered_by;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String[] getDishes() {
        return dishes;
    }

    public void setDishes(String[] dishes) {
        this.dishes = dishes;
    }

    public String[] getCount() {
        return count;
    }

    public void setCount(String[] count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(String availableOn) {
        this.availableOn = availableOn;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getBankHolderName() {
        return bankHolderName;
    }

    public void setBankHolderName(String bankHolderName) {
        this.bankHolderName = bankHolderName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastCount() {
        return lastCount;
    }

    public void setLastCount(String lastCount) {
        this.lastCount = lastCount;
    }

    public String getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getLastTest() {
        return lastTest;
    }

    public void setLastTest(String lastTest) {
        this.lastTest = lastTest;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getHivStatus() {
        return hivStatus;
    }

    public void setHivStatus(String hivStatus) {
        this.hivStatus = hivStatus;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setShowDistance(String showDistance) {
        this.showDistance = showDistance;
    }

    public String getShowDistance() {
        return showDistance;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTribes() {
        return tribes;
    }

    public void setTribes(String tribes) {
        this.tribes = tribes;
    }


    public String getHiv_status() {
        return hiv_status;
    }

    public void setHiv_status(String hiv_status) {
        this.hiv_status = hiv_status;
    }

    public String getLast_test() {
        return last_test;
    }

    public void setLast_test(String last_test) {
        this.last_test = last_test;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }


    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(String minWeight) {
        this.minWeight = minWeight;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getOnlineNow() {
        return onlineNow;
    }

    public void setOnlineNow(String onlineNow) {
        this.onlineNow = onlineNow;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getShowMe() {
        return showMe;
    }

    public void setShowMe(String showMe) {
        this.showMe = showMe;
    }

    public String getPhotosOnly() {
        return photosOnly;
    }

    public void setPhotosOnly(String photosOnly) {
        this.photosOnly = photosOnly;
    }
}
