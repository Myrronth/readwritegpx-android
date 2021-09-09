# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0b] - 2021-09-09
### Added
- Added ability to serialize `GPX` via `GpxSerializer.serialize(gpx: GPX)`
### Changed
- **Breaking change:** Renamed `ReadWriteGpx` to `GpxParser`
- **Breaking change:** Removed `GpxParser.read`, use `GpxParser.parse` instead

## [0.1.4] - 2021-09-08
### Fixed
- Fixed a crash caused by 'magvar' on 'wpt' being an attribute instead of an element.

## [0.1.3] - 2021-09-08
### Added
- Throw NullPointerExceptions when mandatory attributes are missing.
### Fixed
- Fixed a crash when 'magvar' on 'wpt' was not set (magvar is now optional, following the GPX spec). 

## [0.1.2] - 2021-09-07
### Added
- Added documentation 

## [0.1.1] - 2021-09-07
### Fixed
- Fixed [jitpack.io](https://jitpack.io) issues

## [0.1.0] - 2021-09-07
### Added
- Added ability to parse a GPX 1.1 file
