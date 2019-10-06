/*
 * AlibcURLBusMiniAppContext.h 
 *
 * 阿里百川电商
 * 项目名称：阿里巴巴电商 AlibcTradeBiz 
 * 版本号：4.0.0.2
 * 发布时间：2019-09-15
 * 开发团队：阿里巴巴商家服务引擎团队
 * 阿里巴巴电商SDK答疑群号：1488705339  2071154343(阿里旺旺)
 * Copyright (c) 2016-2020 阿里巴巴-淘宝-百川. All rights reserved.
 */

#import <Foundation/Foundation.h>
#import "AlibcLinkPartnerBridge.h"
#import <UIKit/UIKit.h>

@interface AlibcURLBusMiniAppContext : NSObject

// 打开页面URL
@property(nonatomic,copy)NSString *url;

// bizCode (detail/shop/.../unkown)
@property(nonatomic,copy)NSString *bizCode;

// parentVC
@property(nonatomic,weak)UIViewController *sourceViewController;

// 是否需要push
@property(nonatomic,assign)BOOL isNeedPush;

// 淘客参数
@property(nonatomic,strong)AlibcTradeTaokeParams *taokeParams;

@end

