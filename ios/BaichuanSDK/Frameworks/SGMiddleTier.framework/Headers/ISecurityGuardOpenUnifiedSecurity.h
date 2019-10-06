//
//  ISecurityGuardOpenUnifiedSecurity.h
//  SecurityGuardMiddleTier
//
//  Created by chenkong.zh on 2019/07/10.
//  Copyright © 2019年 Li Fengzhong. All rights reserved.
//

#ifndef ISecurityGuardOpenUnifiedSecurity_h
#define ISecurityGuardOpenUnifiedSecurity_h


#import <SecurityGuardSDK/Open/IOpenSecurityGuardPlugin.h>

extern NSInteger const UNIFIED_ENV_ONLINE;
extern NSInteger const UNIFIED_ENV_PRE;
extern NSInteger const UNIFIED_ENV_DAILY;

@protocol ISecurityGuardOpenUnifiedSecurity <NSObject, IOpenSecurityGuardPluginInterface>

/**
 *  init unified security component
 *
 *  @param paramDict <NSString, object> paras
 *    "authCode"            : (NSString*)       [optional] if not set, default search yw_1222.jpg and yw_1222_mwua.jpg
 *  @return init result
 */
-(BOOL) init:(NSDictionary*) paramDict
       error:(NSError* __autoreleasing*)error;

/**
 *  get all security factors with only one function call
 *
 *  @param paramDict <NSString, object> paras
 *    "appkey"              : (NSString*)       [required] appkey for sign
 *    "data"                : (NSString*)       [required] data need to be signed;
 *    "useWua"              : (NSNumber*)       [required] get wua or not (BOOL)value;
 *    "api"                 : (NSString*)       [optional] sign request api, use for risk control in api level
 *    "env"                 : (NSNumber*)       [optional] 0:online, 1:pre, 2:daily if not set default set online
 *    "authCode"            : (NSString*)       [optional] if not set, default search yw_1222.jpg and yw_1222_mwua.jpg
 *    "extendParas"         : (NSDictionary*)   [optional] extra datas e.g. api name, page id, ..
 *  @return sign result dictionary
 *    "x-sign"              : (NSString*)       [required] sign result
 *    "x-mini-wua"          : (NSString*)       [required] mini wua string result
 *    "wua"                 : (NSString*)       [optional] only required when call with high risk business, like [MTOP-SDK useWua]
 *    "x-umt"               : (NSString*)       [optional] umid token for risk control
 *    "x-sgext"             : (NSString*)       [optional] extend wsg result1
 */
-(NSDictionary*) getSecurityFactors:(NSDictionary*) paramDict
                              error:(NSError* __autoreleasing*)error;
@end



#endif /* ISecurityGuardOpenUnifiedSecurity_h */
