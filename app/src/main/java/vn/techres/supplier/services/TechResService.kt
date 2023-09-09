package vn.techres.supplier.services

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import vn.techres.supplier.model.chat.params.AddMemberParams
import vn.techres.supplier.model.chat.params.RemoverMemberParams
import vn.techres.supplier.model.chat.response.*
import vn.techres.supplier.model.params.*
import vn.techres.supplier.model.request.LoginRequest
import vn.techres.supplier.model.response.*


const val BASE_URL = "/api/queues"

interface TechResService {
    //Login Java
    @POST(BASE_URL)
    fun loginJava(
        @Body userParams: UserJavaParams,
    ): Observable<UserResponse> // body data

    //Login NodeJs
    @POST(BASE_URL)
    fun loginNodeJs(
        @Body userParams: UserNodeJsParams,
    ): Observable<UserNodeResponse> // body data

    //Config
    @POST(BASE_URL)
    fun configsJava(
        @Body baseParams: BaseParams,
    ): Observable<ConfigJavaResponse> // body data

    //Config
    @POST(BASE_URL)
    fun getLinkImage(
        @Body baseParams: BaseParams,
    ): Observable<GetLinkImageResponse> // body data

    @POST(BASE_URL)
    fun configsNodeJs(
        @Body baseParams: BaseParams,
    ): Observable<ConfigNodeJsResponse> // body data

    @POST("/api/suppliers/forgotPassword")
    fun forgotPassword(
        @Body loginPostData: LoginRequest,
    ): Observable<LoginResponse> // body data

    @GET("/api/employees/{id}")
    fun user(@Path("id") id: String): Observable<UserResponse>

    @POST(BASE_URL)
    fun editProfileAccount(@Body editProfileAccountParams: EditProfileAccountParams): Observable<UserResponse>

    @POST(BASE_URL)
    fun getListCustomer(@Body baseParams: BaseParams): Observable<CustomerResponse>

    @POST(BASE_URL)
    fun getEmployee(@Body baseParams: BaseParams): Observable<EmployeeResponse>

    @POST(BASE_URL)
    fun addMember(@Body addMemberParams: AddMemberParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getGroupOrder(@Body baseParams: BaseParams): Observable<GroupOrderResponse>

    @POST(BASE_URL)
    fun getMessagePaginationByGroup(@Body baseParams: BaseParams): Observable<MessageByGroupResponse>

    @POST(BASE_URL)
    fun getAllCategorySticker(@Body baseParams: BaseParams): Observable<StickerDataResponse>

    @POST(BASE_URL)
    fun getDetailGroup(@Body baseParams: BaseParams): Observable<DetailGroupResponse>

    @POST(BASE_URL)
    fun getArchiveByGroup(@Body baseParams: BaseParams): Observable<ArchiveByGroupResponse>

    @POST(BASE_URL)
    fun getPinnedMessage(@Body baseParams: BaseParams): Observable<MessageResponse>

    @POST(BASE_URL)
    fun uploadNameFile(@Body baseParams: BaseParams): Observable<FileChatResponse>

    @POST(BASE_URL)
    fun getChangeEmployee(@Body baseParams: BaseParams): Observable<ChangeResponse>

    @POST(BASE_URL)
    fun removeMember(@Body baseParams: RemoverMemberParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getInfoGroup(@Body baseParams: BaseParams): Observable<GetInfoGroupResponse>

    @POST(BASE_URL)
    fun getNotification(@Body baseParams: BaseParams): Observable<NotificationResponse>


    @POST(BASE_URL)
    fun getMessageNotSeen(@Body baseParams: BaseParams): Observable<MessageNotSeenResponse>


    @POST(BASE_URL)
    fun getChangeCanelledBillOrder(@Body baseParams: StatusBillOrderParams): Observable<OrderBillResponse>

    @POST(BASE_URL)
    fun getForgotPassword(@Body forgotPasswordParams: ForgotPasswordParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getChangePassword(@Body changePasswordParams: ChangePasswordParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getVerifyForgotPassword(@Body verifyForgotParams: VerifyForgotParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getChangeOrder(@Body returnBillOrderParams: ReturnBillOrderParams): Observable<OrderResponse>

    @POST(BASE_URL)
    fun getDetailEmloyee(@Body baseParams: BaseParams): Observable<EmployeeProfileResponse>

    @POST(BASE_URL)
    fun getDetailGroupChat(@Body baseParams: BaseParams): Observable<DetailGroupChatResponse>


    @POST(BASE_URL)
    fun getRestaurantContactors(@Body baseParams: BaseParams): Observable<RestaurantContactorsResponse>

    @POST(BASE_URL)
    fun getDebtManager(@Body baseParams: BaseParams): Observable<DebtResponse>

    @POST(BASE_URL)
    fun getCategoryReport(@Body baseParams: BaseParams): Observable<CategoryReportResponse>

    @POST(BASE_URL)
    fun getWareHouseReport(@Body baseParams: BaseParams): Observable<WareHouseReportResponse>

    @POST(BASE_URL)
    fun getOrderReportByRestaurant(@Body baseParams: BaseParams): Observable<OrderReportByRestaurantResponse>


    @POST(BASE_URL)
    fun getCustomerGetById(@Body baseParams: BaseParams): Observable<CustomerGetByIdResponse>

    @POST(BASE_URL)
    fun getListBranchGetByIdCustomer(@Body baseParams: BaseParams): Observable<BranchGetByIdCustomerResponse>

    @POST(BASE_URL)
    fun getUser(@Body baseParams: BaseParams): Observable<UserResponse>

    @POST(BASE_URL)
    fun getCreateEmployee(@Body params: CreateStaffParams): Observable<EmployeeProfileResponse>


    @POST(BASE_URL)
    fun getSupplierDevice(@Body deviceParams: DeviceParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getPushOutToken(@Body deviceParams: PushOutTokenParams): Observable<BaseResponse>


    @POST(BASE_URL)
    fun getListUnits(@Body baseParams: BaseParams): Observable<UnitsResponse>

    @POST(BASE_URL)
    fun getListGenusItems(@Body baseParams: BaseParams): Observable<GenusItemsResponse>

    @POST(BASE_URL)
    fun getListReason(@Body baseParams: BaseParams): Observable<ReasonResponse>

    @POST(BASE_URL)
    fun getListReceipts(@Body baseParams: BaseParams): Observable<ReceiptsResponse>

    @POST(BASE_URL)
    fun getEditGenusItems(@Body genusItemsParams: EditGenusItemsParams): Observable<CreateGenusItemsResponse>

    @POST(BASE_URL)
    fun changeStatusUnits(@Body baseParams: BaseParams): Observable<ChangeStatusUnitsResponse>

    @POST(BASE_URL)
    fun getListSpecification(@Body baseParams: BaseParams): Observable<SpecificationResponse>

    @POST(BASE_URL)
    fun getListWareHouse(@Body baseParams: BaseParams): Observable<WareHouseResponse>

    @POST(BASE_URL)
    fun changeStatusSpecification(@Body baseParams: BaseParams): Observable<ChangeStatusSpecificationResponse>

    @POST(BASE_URL)
    fun createAndUpdateSpecification(@Body createSpecificationParams: CreateAndUpdateSpecificationParams): Observable<CreateAndUpdateSpecificationResponse>

    @POST(BASE_URL)
    fun createReceipts(@Body createReceiptsParams: CreateReceiptsParams): Observable<ReceiptsResponse>

    @POST(BASE_URL)
    fun getListUnitsExchange(@Body baseParams: BaseParams): Observable<UnitsExchangeResponse>

    @POST(BASE_URL)
    fun getListAdditionFees(@Body baseParams: BaseParams): Observable<DetailBillResponse>

    @POST(BASE_URL)
    fun getListRolesExchange(@Body baseParams: BaseParams): Observable<RolesExchangeResponse>

    @POST(BASE_URL)
    fun updateUnits(@Body updateUnitsParams: UpdateUnitsParams): Observable<CreateAndUpdateUnitsResponse>

    @POST(BASE_URL)
    fun editEmployee(@Body params: EditStaffParams): Observable<EditStaffResponse>

    @POST(BASE_URL)
    fun createUnits(@Body createUnitsParams: CreateUnitsParams): Observable<CreateAndUpdateUnitsResponse>

    @POST(BASE_URL)
    fun createGensItems(@Body createGenusItemsParams: CreateGenusItemsParams): Observable<CreateGenusItemsResponse>

    @POST(BASE_URL)
    fun feedBack(@Body feedBackParams: FeedBackParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getRevenueDetail(@Body baseParams: BaseParams): Observable<RevenueDetailResponse>

    @POST(BASE_URL)
    fun getOverViewOrderInDay(@Body baseParams: BaseParams): Observable<OverViewOrderInDayResponse>

    @POST(BASE_URL)
    fun getRevenueWeek(@Body baseParams: BaseParams): Observable<RevenueWeekResponse>

    @POST(BASE_URL)
    fun getRevenueThisMonth(@Body baseParams: BaseParams): Observable<RevenueThisMonthResponse>

    @POST(BASE_URL)
    fun getRevenueThreeMonths(@Body baseParams: BaseParams): Observable<RevenueThreeMonthsResponse>

    @POST(BASE_URL)
    fun getRevenueThisYear(@Body baseParams: BaseParams): Observable<RevenueThisYearResponse>

    @POST(BASE_URL)
    fun getOrderReport(@Body baseParams: BaseParams): Observable<OrderReportResponse>

    @POST(BASE_URL)
    fun getOrderTimeReport(@Body baseParams: BaseParams): Observable<OrderTimeReportResponse>

    @POST(BASE_URL)
    fun getMaterialsReport(@Body baseParams: BaseParams): Observable<MaterialsReportResponse>

    @POST(BASE_URL)
    fun getRevenueLastYear(@Body baseParams: BaseParams): Observable<RevenueLastYearResponse>

    @POST(BASE_URL)
    fun getListCategoryActive(@Body baseParams: BaseParams): Observable<CategoryActiveResponse>

    @POST(BASE_URL)
    fun getListCancelReturn(@Body baseParams: BaseParams): Observable<ReportCancelReturnResponse>

    @POST(BASE_URL)
    fun getListPaymentRequest(@Body baseParams: BaseParams): Observable<PaymentRequestResponse>

    @POST(BASE_URL)
    fun getDetailPaymentRequest(@Body baseParams: BaseParams): Observable<DetailPaymentRequestResponse>
  @POST(BASE_URL)
    fun getDetailBillPaymentRequest(@Body baseParams: BaseParams): Observable<DetailBillPaymentRequestResponse>

    @POST(BASE_URL)
    fun getChangePaymentRequest(@Body changePaymentRequestParams: ChangePaymentRequestParams): Observable<DetailPaymentRequestResponse>

    @POST(BASE_URL)
    fun getReportOrderAmount(@Body baseParams: BaseParams): Observable<ReportOrderAmountResponse>

    @POST(BASE_URL)
    fun getListDebtReceivable(@Body baseParams: BaseParams): Observable<DebtReceivableResponse>

    @POST(BASE_URL)
    fun getListCategoryPause(@Body baseParams: BaseParams): Observable<CategoryPauseResponse>

    @POST(BASE_URL)
    fun getListMaterial(@Body baseParams: BaseParams): Observable<ListMaterialManagerResponse>

    @POST(BASE_URL)
    fun getListMaterialDetail(@Body baseParams: BaseParams): Observable<DetailBillWareHouseResponse>

    @POST(BASE_URL)
    fun getListOrderBill(@Body baseParams: BaseParams): Observable<OrderBillResponse>

    @POST(BASE_URL)
    fun getListSumMaterialOrder(@Body baseParams: BaseParams): Observable<SumMaterialOrderResponse>

    @POST(BASE_URL)
    fun getListDay(@Body baseParams: BaseParams): Observable<DayOrderResponse>

    @POST(BASE_URL)
    fun getBillOrderMaterial(@Body baseParams: BaseParams): Observable<BillOrderMaterialResponse>

    @POST(BASE_URL)
    fun getDetailOrderMaterial(@Body baseParams: BaseParams): Observable<DetailListMatialOrderResponse>

    @POST(BASE_URL)
    fun getDetailBillOrder(@Body baseParams: BaseParams): Observable<DetailBillOrderResponse>

    @POST(BASE_URL)
    fun getDetailBillOrderAll(@Body baseParams: BaseParams): Observable<DetailOrderBillAllResponse>

    @POST(BASE_URL)
    fun getDetailMaterial(@Body baseParams: BaseParams): Observable<DetailMaterialResponse>


    @POST(BASE_URL)
    fun getListOrderWaitRestaurantConfirm(@Body baseParams: BaseParams): Observable<OrderResponse>

    @POST(BASE_URL)
    fun getListOrder(@Body baseParams: BaseParams): Observable<OrderResponse>

    @POST(BASE_URL)
    fun getListOrderDelivering(@Body baseParams: BaseParams): Observable<OrderResponse>

    @POST(BASE_URL)
    fun getListOrderCompleted(@Body baseParams: BaseParams): Observable<OrderResponse>

    @POST(BASE_URL)
    fun getCreatePayment(@Body createPaymentRequestParams: CreatePaymentRequestParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getUpdatePayment(@Body updatePaymentRequestParams: UpdatePaymentRequestParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getListBranch(@Body baseParams: BaseParams): Observable<BranchResponse>

    @POST(BASE_URL)
    fun getListOrderCanceled(@Body baseParams: BaseParams): Observable<OrderResponse>

    @POST(BASE_URL)
    fun getListOrderRestaurant(@Body baseParams: BaseParams): Observable<OrderRestaurantResponse>

    @POST(BASE_URL)
    fun getListReturnsOrder(@Body baseParams: BaseParams): Observable<ReturnsOrderResponse>

    @POST(BASE_URL)
    fun changeStatusMaterial(@Body baseParams: BaseParams): Observable<ChangeStatusMaterialResponse>

    @POST(BASE_URL)
    fun createMaterial(@Body params: CreateMaterialParams): Observable<CreateMaterialResponse>


    @POST(BASE_URL)
    fun createWareHouse(@Body createWareHouseParams: CreateWareHouseParams): Observable<CreateWareHouseResponse>

    @POST(BASE_URL)
    fun getDetailMaterialWareHouse(@Body baseParams: BaseParams): Observable<DetailWarehouseResponse>

    @POST(BASE_URL)
    fun ConfirmBillOrder(@Body createWareHouseParams: ConfirmBillOrderParams): Observable<OrderResponse>

    @POST(BASE_URL)
    fun UpdateBillOrder(@Body udapteBillOrderParams: UdapteBillOrderParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getListMaterialSelected(@Body baseParams: BaseParams): Observable<MaterialSelectedResponse>
    @POST(BASE_URL)
    fun getConfirmWareHouse(@Body statusWareHouseParams: StatusWareHouseParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun updateWareHouse(@Body updateWareHouseParams: UpdateWareHouseParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getChatNotSeenGroupChat(@Body baseParams: BaseParams): Observable<NotSeenGroupChatResponse>

    @POST(BASE_URL)
    fun getChatNotSeenGroupChatOrder(@Body baseParams: BaseParams): Observable<ChatNotSeenGroupResponse>

    @POST(BASE_URL)
    fun getContactOrder(@Body baseParams: BaseParams): Observable<ContactOrderResponse>

    @POST(BASE_URL)
    fun getVersion(@Body baseParams: BaseParams): Observable<VersionResponse>

    @POST(BASE_URL)
    fun getDelete(@Body baseParams: BaseParams): Observable<BaseResponse>

    @POST(BASE_URL)
    fun getStatus(@Body statusAdditionFeesParams: StatusAdditionFeesParams): Observable<BaseResponse>

}

