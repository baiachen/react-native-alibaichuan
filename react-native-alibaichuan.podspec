require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name          = package['name']
  s.version       = package['version']
  s.summary        = "alibaichuan"
  s.homepage       = "https://github.com/baiachen/react-native-alibaichuan"
  s.description   = package['description']
  s.license       = package['license']
  s.author        = package['author']
  s.platform      = :ios, "9.0"
  s.source        = { :git => "https://github.com/baiachen/react-native-alibaichuan.git", :tag => "master" }
  s.source_files  = "ios/BCBridge.{h,m}", "ios/BCWebManager.{h,m}", "ios/BCWebView.{h,m}", "ios/RNAlibcSdk.{h,m}"
  s.requires_arc  = true
  s.resources = "ios/BaichuanSDK/Resources/*", "ios/BaichuanSDK/mtopsdk_configuration.plist"
  s.vendored_frameworks = "ios/BaichuanSDK/Frameworks/*"
  s.frameworks = "CoreTelephony", "CoreMotion"
  s.libraries = "sqlite3", "c++", "z"

  s.dependency "React"

end

