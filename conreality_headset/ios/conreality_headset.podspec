# See: https://guides.cocoapods.org/syntax/podspec.html
Pod::Spec.new do |s|
  s.name             = 'conreality_headset'
  s.version          = '0.1.0'
  s.summary          = 'Headset audio and text-to-speech support.'
  s.description      = <<-DESC
Headset audio and text-to-speech support for Conreality live-action games.
                       DESC
  s.homepage         = 'https://github.com/conreality/conreality-plugins/tree/master/conreality_headset'
  s.license          = { :file => '../UNLICENSE' }
  s.author           = { 'Conreality Team' => 'conreality@googlegroups.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'

  s.ios.deployment_target = '8.0'
end

