package com.guide.test.setup.response;

import com.guide.infra.client.dto.UserFindApiResponse;
import com.guide.infra.client.dto.UserFindApiResponse.Data;
import com.guide.infra.client.dto.UserFindApiResponse.Support;

public class UserClientSetup {

    private static final String EMAIL = "george.bluth@reqres.in";
    private static final String FIRST_NAME = "George";
    private static final String LAST_NAME = "Bluth";
    private static final String AVATAR = "https://reqres.in/img/faces/1-image.jpg";

    public static UserFindApiResponse buildUserFindApiResponse(long id) {
        return buildUserFindApiResponse(id, EMAIL, FIRST_NAME, LAST_NAME, AVATAR);
    }

    private static UserFindApiResponse buildUserFindApiResponse(
            long id, String email, String firstName, String lastName, String avatar) {
        Data data = new Data(id, email, firstName, lastName, avatar);
        Support support =
                new Support(
                        "https://reqres.in/#support-heading",
                        "To keep ReqRes free, contributions towards server costs are appreciated!");
        return new UserFindApiResponse(data, support);
    }
}
