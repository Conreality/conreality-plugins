# See: https://guides.cocoapods.org/syntax/podspec.html
Pod::Spec.new do |s|
  s.name             = 'conreality_beacon'
  s.version          = '0.0.1'
  s.summary          = 'Radio beacon gadget support.'
  s.description      = <<-DESC
Radio beacon gadget support for Conreality live-action games.
                       DESC
  s.homepage         = 'https://github.com/conreality/conreality-plugins/tree/master/conreality_beacon'
  s.license          = { :file => '../UNLICENSE' }
  s.author           = { 'Conreality Team' => 'conreality@googlegroups.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'

  s.ios.deployment_target = '8.0'
end
