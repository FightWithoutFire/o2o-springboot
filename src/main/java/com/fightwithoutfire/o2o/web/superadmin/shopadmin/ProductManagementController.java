package com.fightwithoutfire.o2o.web.superadmin.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fightwithoutfire.o2o.dto.ImageHolder;
import com.fightwithoutfire.o2o.dto.ProductExecution;
import com.fightwithoutfire.o2o.entity.Product;
import com.fightwithoutfire.o2o.entity.ProductCategory;
import com.fightwithoutfire.o2o.entity.Shop;
import com.fightwithoutfire.o2o.enums.ProductStateEnum;
import com.fightwithoutfire.o2o.service.ProductCategoryService;
import com.fightwithoutfire.o2o.service.ProductService;
import com.fightwithoutfire.o2o.util.CodeUtil;
import com.fightwithoutfire.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopadmin")
public class ProductManagementController {

	@Autowired
	private ProductCategoryService productcategory;

	@Autowired
	private ProductService productService;

	private static final int IMAGEMAXCOUNT=6;

	@PostMapping(value = "/addproduct")
	private Map<String,Object> addProduct(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误验证码");
			return modelMap;
		}
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		MultipartHttpServletRequest multipartRequest=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImageList=new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if(multipartResolver.isMultipart(request)) {
				multipartRequest=(MultipartHttpServletRequest)request;
				CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartRequest.getFile("thumbnail");
				thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
				for(int i=0;i<IMAGEMAXCOUNT;i++) {
					CommonsMultipartFile productImgFile=(CommonsMultipartFile)multipartRequest
						.getFile("productImg"+i);
					if(productImgFile!=null) {
						ImageHolder productImg=new ImageHolder(productImgFile.getOriginalFilename()
								,productImgFile.getInputStream());
						productImageList.add(productImg);
					}else {
						break;
					}
				}
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传不能为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			product=mapper.readValue(productStr, Product.class);
			
 		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if(product!=null&&thumbnail!=null&&productImageList.size()>0) {
			try {
				Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
				Shop shop=new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				ProductExecution pe=productService.addProduct(product, thumbnail, productImageList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
					return modelMap;
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}
		return modelMap;
		
	}

	@GetMapping(value = "/getproductbyid")
	private Map<String,Object> getProductById(@RequestParam Long productId){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		if(productId>-1) {
			Product product=productService.getProductById(productId);
			List<ProductCategory> productCategoryList=productcategory.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product",product);
			modelMap.put("productCategoryList",productCategoryList);
			modelMap.put("success",true);
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","empty productId");
		}
		return modelMap;
	}

	@PostMapping(value = "/modifyproduct")
	private Map<String,Object> modifyProduct(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		boolean statusChange=HttpServletRequestUtil.getBoolean(request, "statusChange");
		if(!statusChange&&!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg","输入错误的验证码");
			return modelMap;
			
		}
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResovler=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if(multipartResovler.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}
		} catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.toString());
			return modelMap;
		}
		try {
			String productStr=HttpServletRequestUtil.getString(	request, "productStr");
			product=mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.toString());
			return modelMap;
		}
		if(product!=null) {
			try {
				Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					
				}else {
					modelMap.put("success",false);
					modelMap.put("errMsg",pe.getStateInfo());
					return modelMap;
				}
				
			}catch (Exception e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.toString());
				return modelMap;

			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请输入商品信息");
		}
		return modelMap;
	}

	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws IOException {
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
		CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartRequest.getFile("thumbnail");
		if(thumbnailFile!=null) {
			thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
		}
		for(int i=0;i<IMAGEMAXCOUNT;i++) {
			CommonsMultipartFile productImgFile=(CommonsMultipartFile)multipartRequest.getFile("productImg"+i);
			if(productImgFile!=null) {
				ImageHolder imageHolder=new ImageHolder(productImgFile.getOriginalFilename(),
						productImgFile.getInputStream());
				productImgList.add(imageHolder);
			}else {
				break;
			}
		}
		return thumbnail;
	}

	@GetMapping(value = "/getproductlistbyshop")
	private Map<String,Object> getProductListByShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
		if(pageIndex>-1&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)) {
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId")==null?
					-1L:HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
			ProductExecution pe=productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList",pe.getProductList());
			modelMap.put("count",pe.getCount());
			modelMap.put("success",true);
			
		}else {
			modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
			modelMap.put("success",false);
		}
		return modelMap;
		
	}

	private Product compactProductCondition(long shopId, long productCategoryId, String productName) {
		Product productCondition=new Product();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId!=-1L) {
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName!=null) {
			productCondition.setProductName(productName);
		}
		return productCondition;
	}
	
}
