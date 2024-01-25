import 'dart:io';

import 'args_for_ios.dart';

class ArgsForKillNotification {
  final String title;
  final String description;
  /* 디아콘 추가 시작 */
  final String? channelId;
  final String? channelName;
  /* 디아콘 추가 끝 */
  final ArgsForIos? argsForIos;

  ArgsForKillNotification({
    required this.title,
    required this.description,
    /* 디아콘 추가 시작 */
    this.channelId,
    this.channelName,
    /* 디아콘 추가 끝 */
    this.argsForIos,
  });

  Map<String, dynamic> toJson() {
    if (argsForIos != null && Platform.isIOS) {
      return {
        'title': title,
        'description': description,
        'argsForIos': argsForIos!.toJson(),
        'useDefaultSound': argsForIos!.useDefaultSound,
        'interruptionLevel': argsForIos!.interruptionLevel.index,
      };
    }
    return {
      'title': title,
      'description': description,
      'channelId': channelId,
      'channelName': channelName,
    };
  }
}
