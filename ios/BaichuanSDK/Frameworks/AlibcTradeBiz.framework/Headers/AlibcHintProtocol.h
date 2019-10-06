/*
 * AlibcHintProtocol.h 
 *
 * 阿里百川电商
 * 项目名称：阿里巴巴电商 AlibcTradeBiz 
 * 版本号：4.0.0.2
 * 发布时间：2019-09-15
 * 开发团队：阿里巴巴商家服务引擎团队
 * 阿里巴巴电商SDK答疑群号：1488705339  2071154343(阿里旺旺)
 * Copyright (c) 2016-2020 阿里巴巴-淘宝-百川. All rights reserved.
 */

#ifndef ALiHintProtocal_h
#define ALiHintProtocal_h

@protocol AlibcHintProtocol <NSObject>

//根据bizid返回组件的全量权限点
- (NSArray<NSString *> *)getHintList:(NSString *)bizID;

//上报组件返回的权限点有缺失
- (void)reportHintLost:(NSString *)bizID hintId:(NSString *)hintId;
@end

#endif /* ALiMtopHintProtocol_h */
